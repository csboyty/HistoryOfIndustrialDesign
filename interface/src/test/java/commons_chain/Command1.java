package commons_chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

public class Command1 implements Filter {

	@Override
	public boolean execute(Context context) throws Exception {

		System.out.println("command1");
		return Command.CONTINUE_PROCESSING;
	}

	@Override
	public boolean postprocess(Context context, Exception exception) {
		System.out.println("command1 postprocess");
		if(exception!=null){
			System.out.println(exception);
		}
		return Command.CONTINUE_PROCESSING;
	}
	

	
}
