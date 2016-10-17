package commons_chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

public class Command2 implements Filter {

	@Override
	public boolean execute(Context context) throws Exception {
		System.out.println("command2");
//		throw new Exception("command2 error");
		return Command.CONTINUE_PROCESSING;
	}

	@Override
	public boolean postprocess(Context context, Exception exception) {
		System.out.println("command2 postprocess");
		if(exception!=null){
			System.out.println(exception);
		}
		throw new RuntimeException("command2 postprocess error");
//		return Command.PROCESSING_COMPLETE;
	}
	
	
	
}
