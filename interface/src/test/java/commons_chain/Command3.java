package commons_chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class Command3 implements Command{

	@Override
	public boolean execute(Context context) throws Exception {
		System.out.println("command3");
		return Command.PROCESSING_COMPLETE;
	}
	
	

}
