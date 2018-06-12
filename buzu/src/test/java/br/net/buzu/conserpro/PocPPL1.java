package br.net.buzu.conserpro;

import br.net.buzu.Buzu;

public class PocPPL1 {
	
	public static void main(String[] args) {
		runParse(100, 4);
	}
	
	private static void runParse(int loop, int itens) {
		Buzu buzu = new Buzu();
		Due due = PocTime.createDue(4);
		String dueStr = buzu.toPpl(due);
		System.out.println("DUE String size:" + dueStr.length());
		System.out.println(dueStr);
		long t0 = System.currentTimeMillis();
		for (int i=0; i < loop; i++) {
			buzu.fromPpl(dueStr, Due.class);
		}
		long t1 = System.currentTimeMillis();
		long time = t1-t0;
		System.out.println("Parsing da Due time:" + time);
	}

}
