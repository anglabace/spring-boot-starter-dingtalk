/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.smslib.spring.boot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LEDBlink {
	
	// 获取全局Gpio 引脚控制器对象
	public static final GpioController gpio = GpioFactory.getInstance();

	public static void main(String[] rags) throws InterruptedException {

		// 定义编号为0的引脚为数字输出引脚，初始化为低电平
		GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "", PinState.LOW);

		while (true) {

			myLed.high();
			Thread.sleep(500);
			myLed.low();
			Thread.sleep(500);
			
		}

	}

}