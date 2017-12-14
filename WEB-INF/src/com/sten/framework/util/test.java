package com.sten.framework.util;

import java.math.BigDecimal;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigDecimal call_duration = new BigDecimal(476);
		BigDecimal out_local_cost = new BigDecimal(0.06);
		BigDecimal out_local_cycle = new BigDecimal(60);
		BigDecimal out_local_min_cycle = new BigDecimal(6);
		BigDecimal out_local_min_cost = out_local_cost.divide(out_local_cycle
				.divide(out_local_min_cycle));

		BigDecimal money1 = call_duration.divide(out_local_cycle, 0,
				BigDecimal.ROUND_DOWN).multiply(out_local_cost);

		BigDecimal midValue = call_duration.divide(out_local_cycle, 0,
				BigDecimal.ROUND_DOWN).multiply(out_local_cycle);

		BigDecimal money2 = call_duration.subtract(midValue)
				.divide(out_local_min_cycle, 0, BigDecimal.ROUND_UP)
				.multiply(out_local_min_cost);
		int a = 1;

	}

}
