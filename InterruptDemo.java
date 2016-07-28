// Copyright 2016 Brian Orr
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// This class demonstrates a portable way for Java code to remain responsive to asynchronous
// signals (such as Ctrl-C) when called from the Matlab command line or a Matlab script. There is no
// build-time dependency on the Matlab JMI jar which should allow it to build (ex: on CI servers)
// without a Matlab installation. The Matlab JMI jar is required at run time.
// 
// Tested with Matlab version 9.0.0.341360 (R2016a).

public class InterruptDemo {
    public static void blockForever() {
        Class ML = null;
        Class MLE = null;

        try {
            // Suppress warning since we can't link against the Matlab JMI jar.
            @SuppressWarnings("unchecked")
            Class ml = java.lang.Class.forName("com.mathworks.jmi.Matlab");
            ML = ml;

            MLE = java.lang.Class.forName("com.mathworks.jmi.MatlabException");
        } catch (ClassNotFoundException e) {
            System.out.println("Not running in Matlab. Carry on.");
        }

        // Running native or in a Matlab instance?
        if (ML == null) {
            try {
                while (true) {
                    java.lang.Thread.sleep(Long.MAX_VALUE);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else {
            try {
                // Suppress warning since we're calling a method on a non-generic typed object.
                @SuppressWarnings("unchecked")
                java.lang.reflect.Method mtEval = ML.getMethod("mtEval", String.class, int.class);

                // Call drawnow() to process Matlab events and remain responsive to "Ctrl-C" while
                // inifinite-looping in a Java method.
                while (true) {
                    mtEval.invoke(null, "drawnow", 0);
                    java.lang.Thread.sleep(100);
                }
            } catch (Exception e) {
                Throwable t = e.getCause();
                if (t != null && t.getClass().equals(MLE)) {
                    System.out.println("Matlab Ctrl-C");
                } else {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public static void main(String args[]) {
        InterruptDemo.blockForever();
    }
}
