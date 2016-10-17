package commons_chain;

import org.apache.commons.chain.impl.ChainBase;
import org.apache.commons.chain.impl.ContextBase;

public class CommonsChainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChainBase chain = new ChainBase();
		chain.addCommand(new Command1());
		chain.addCommand(new Command2());
		chain.addCommand( new Command3());
		try {
			boolean result =chain.execute(new ContextBase());
			System.out.println(result);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
