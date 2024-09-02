package etlpipelines;

import java.util.Objects;
import java.util.Properties;

import cascading.tuple.Fields;

import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;

import cascading.scheme.hadoop.TextDelimited;
// import cascading.scheme.Scheme;

import cascading.pipe.Each;
import cascading.pipe.Pipe;
import cascading.pipe.CoGroup;
import cascading.pipe.GroupBy;
import cascading.pipe.HashJoin;

import cascading.flow.FlowDef;

import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;

import cascading.property.AppProps;

class NullChecker {
	public static <T> T[] check(T[] array, int minLength) throws NullPointerException {
		if (array == null || array.length < minLength) {
			throw new NullPointerException("Array does not have enough values.");
		}

		for (int i=0; i<array.length; ++i) {
			Objects.requireNonNull(array[i]);
		}
		return array;
	}
}

class PropertiesGenerator {
	public static Properties getApplicationConfigured() {
    	Properties properties = new Properties();

		AppProps.setApplicationJarClass(properties, Main.class);
		AppProps.setApplicationName(properties, "Data Pipelines with Cascading Java");
		AppProps.addApplicationTag(properties, "data-engineering");
		AppProps.addApplicationTag(properties, "java-programming");

		return properties;
	}
}
// public interface FileETLPipeline {

// }

public class Main {
	public static void main(String[] args) {
		String[] argsChecked = NullChecker.check(args, 4);

		String documentPath = args[0], countPath = args[1],
				stopWordPath = args[2], resultPath = args[3];

		// var tsvData = 

		Tap documentTap = new Hfs(new TextDelimited(true, "\t" ), documentPath);
		Tap countTap = new Hfs(new TextDelimited(true, "\t" ), countPath);
		Tap resultTap = new Hfs(new TextDelimited( true, "\t" ), resultPath);
		Tap stopTap = new Hfs(new TextDelimited(new Fields( "stop" ), true, "\t"), stopPath);

		// regex operation to split the "document" into a token stream
		Fields token = new Fields("token"), text = new Fields("text");
		RegexSplitGenerator splitter = new RegexSplitGenerator( token, "[ \\[\\]\\(\\),.]" );
		Fields fieldSelector = new Fields("doc_id", "token");

		Pipe docPipe = new Each("token", text, splitter, fieldSelector);

		// perform a left join to remove stop words
		Pipe stopPipe = new Pipe( "stop" );
		Pipe tokenPipe = new HashJoin( docPipe, token, stopPipe, stop, new LeftJoin() );
		tokenPipe = new Each( tokenPipe, stop, new RegexFilter( "^$" ) );
		tokenPipe = new Retain( tokenPipe, fieldSelector );

		// operation to clean up the token stream
		Fields scrubArguments = new Fields("doc_id", "token");
		docPipe = new Each(docPipe, scrubArguments, new Scrubber( scrubArguments ), Fields.RESULTS);

		// token counts for document frequency-DF
		Pipe dfPipe = new Unique("DF", tokenPipe, Fields.ALL);
		Fields df_count = new Fields("df_count");
		dfPipe = new CountBy(dfPipe, token, df_count);

		Fields df_token = new Fields( "df_token" ),
				lhs_join = new Fields( "lhs_join" );
		dfPipe = new Rename( dfPipe, token, df_token );
		dfPipe = new Each( dfPipe, new Insert( lhs_join, 1 ), Fields.ALL );

		// calculate number of documents
		Fields doc_id = new Fields( "doc_id" );
		Fields tally = new Fields( "tally" );
		Fields rhs_join = new Fields( "rhs_join" );
		Fields n_docs = new Fields( "n_docs" );
		Pipe dPipe = new Unique( "D", tokenPipe, doc_id );
		dPipe = new Each( dPipe, new Insert( tally, 1 ), Fields.ALL );
		dPipe = new Each( dPipe, new Insert( rhs_join, 1 ), Fields.ALL );
		dPipe = new SumBy( dPipe, rhs_join, tally, n_docs, long.class );

		// calculate the TF-IDF weights, per token, per document
		Fields tfidf = new Fields( "tfidf" );
		String expression = "(double)tf_count * Math.log( (double) n_docs / ( 1.0 + df_count ) )";
		ExpressionFunction tfidfExpression = new ExpressionFunction( tfidf, expression, Double.class );
		Fields tfidfArguments = new Fields( "tf_count", "df_count", "n_docs" );
		tfidfPipe = new Each( tfidfPipe, tfidfArguments, tfidfExpression, Fields.ALL );

		fieldSelector = new Fields( "tf_token", "doc_id", "tfidf" );
		tfidfPipe = new Retain( tfidfPipe, fieldSelector );
		tfidfPipe = new Rename( tfidfPipe, tf_token, token );

		FlowDef flowDefine = FlowDef.flowDef().setName("Data Pipelines Sample")
											.addSource(docPipe, docTap).addSource(stopPipe, stopTap)
											.addTailSink( tfidfPipe, resultTap ).addTailSink( wcPipe, countTap );

		Properties properties = PropertiesGenerator.getApplicationConfigured();
		FlowConnector flowConnector = new Hadoop2MR1FlowConnector(properties);

		flowConnector.connect(flowDefine).complete();
	}
}