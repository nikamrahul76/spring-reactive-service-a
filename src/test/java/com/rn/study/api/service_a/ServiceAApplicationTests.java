package com.rn.study.api.service_a;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceAApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void test() {
		int[][] array = {
				{1, 2},
				{2, 5},
				{4, 3}
		};

		System.out.println(averageWaitingTime(array));
    }

    public double averageWaitingTime(int[][] customers) {
        long currentTime = 0;  // Chef's next free time
        long totalWait   = 0;  // Sum of all waiting times

        for (int[] customer : customers) {
            int arrival = customer[0];
            int prep    = customer[1];

            // If the chef is idle before this customer arrives, jump to the arrival time
            if (currentTime < arrival) {
                currentTime = arrival;
            }

            // Chef prepares the food (add preparation time)
            currentTime += prep;

            // Waiting time = finish time - arrival time
            totalWait += (currentTime - arrival);
        }

        // Return the average as a double
        return (double) totalWait / customers.length;
    }

}
