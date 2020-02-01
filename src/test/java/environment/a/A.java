package environment.a;

import environment.b.B;

/**
 * aClass_01
 * @cmntGroup AClassGroup
 * @inheritGroup BMethodGroup
 */
public class A {

    public void aMethod01(String a, String b) {
        System.out.println(a);
        System.out.println(b);
        //@cmnt aMethod_01_01

        B testB = new B();
        if (true) {
            /*aMethod_01_02*/
            testB.bMethod02("a ", "b");
        }

        /**
         * aMethod_01_03
         * @cmnt
         */
    }

    public String aMethod02(String a, String b) {
        //@cmnt aMethod_02_01
        return a.concat(b);
    }

}
