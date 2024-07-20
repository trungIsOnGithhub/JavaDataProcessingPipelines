package etlpipelines;

import java.util.Objects;

import cascading.tuple.Fields;

import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;

import cascading.pipe.Pipe;

import cascading.flow.FlowDef;

import cascading.flow.hadoop2.Hadoop2MR1FlowConnector;

class NullChecker {
	public static <T extends Serializable> T[] check(T[] array, int minLength) throws NullPointerException {
		if (array == null || array.length < minLength) {
			throw NullPointerException("Array does not have enough values.");
			return array;
		}

		for (int i=0; i<array.length; ++i) {
			Objects.requireNonNull(array[i]);
		}
		return array;
	}
}

// public interface FileETLPipeline {

// }

public class Main {
	public static void main(String[] args) {
		String[] argsChecked = NullChecker.check(args, 2);

		String inFilePath = args[1], outFilePath = args[2];

		Fields tsvDataFields = new TextDelimited(true, "\t" );

		Tap sourceTap = new Hfs(tsvDataFields, inFilePath);
		Tap destTap = new Hfs(tsvDataFields, outFilePath);

		Pipe copyPipe = new Pipe("copy");

		FlowDef flowDefine = FlowDef.flowDef().setName("File Copier").addSource(copyPipe, sourceTap).addTailSink(copyPipe, destTap);

		Properties properties = new Properties();
		AppProps.setApplicationJarClass(properties, Main.class);
		AppProps.setApplicationName(properties, "ETLPipeline");
		AppProps.addApplicationTag( properties, "technology:CascadingHadoop" );

		Hadoop2MR1FlowConnector flowConnector = new Hadoop2MR1FlowConnector( properties );

		flowConnector.connect(flowDefine).complete();
	}
}