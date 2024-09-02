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

		// operation to clean up the token stream
		Fields scrubArguments = new Fields("doc_id", "token");
		docPipe = new Each(docPipe, scrubArguments, new Scrubber( scrubArguments ), Fields.RESULTS);

		// token counts for document frequency-DF
		Pipe dfPipe = new Unique("DF", tokenPipe, Fields.ALL);
		Fields df_count = new Fields( "df_count" );
		dfPipe = new CountBy( dfPipe, token, df_count );

		Fields df_token = new Fields( "df_token" );
		Fields lhs_join = new Fields( "lhs_join" );
		dfPipe = new Rename( dfPipe, token, df_token );
		dfPipe = new Each( dfPipe, new Insert( lhs_join, 1 ), Fields.ALL );

		FlowDef flowDefine = FlowDef.flowDef().setName("File Copier").addSource(copyPipe, sourceTap).addTailSink(copyPipe, destTap);

		Properties properties = PropertiesGenerator.getApplicationConfigured();
		FlowConnector flowConnector = new Hadoop2MR1FlowConnector(properties);

		flowConnector.connect(flowDefine).complete();
	}
}