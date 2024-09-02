package etlpipelines;

import java.util.Objects;
import java.util.Properties;

import cascading.tuple.Fields;

import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;

import cascading.scheme.hadoop.TextDelimited;
// import cascading.scheme.Scheme;

import cascading.pipe.Pipe;

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

		String documentPath = args[0], countPath = args[1];
		String stopWordPath = args[2], resultPath = args[3];

		// var tsvData = 

		Tap sourceTap = new Hfs(new TextDelimited(true, "\t" ), inFilePath);
		Tap destTap = new Hfs(new TextDelimited(true, "\t" ), outFilePath);

		Pipe copyPipe = new Pipe("copy");

		FlowDef flowDefine = FlowDef.flowDef().setName("File Copier").addSource(copyPipe, sourceTap).addTailSink(copyPipe, destTap);

		Properties properties = PropertiesGenerator.getApplicationConfigured();
		FlowConnector flowConnector = new Hadoop2MR1FlowConnector(properties);

		flowConnector.connect(flowDefine).complete();
	}
}