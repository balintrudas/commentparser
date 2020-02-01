package environment.b;

import environment.a.A;
import com.github.balintrudas.commentparser.marker.group.annotation.CmntGroup;

public class B {

    //bClass_01
    private String justTestProperty;

    /**
     * bClass_02
     * @param a
     * @param b
     * @return
     */
    @CmntGroup(value = "BMethodGroup", inherit = "AClassGroup")
    public String bMethod01(String a, String b){
        System.out.println(a);
        /// bMethod_01_01
        //@cmnt  bMethod_01_02
        ///  bMethod_01_03
        System.out.println(b);
        A testA = new A();
        return testA.aMethod02("a ", "b");
    }

    /*bClass_03*/
    public String bMethod02(String a, String b){
        //@cmnt bMethod_02_01
        return bMethod01(a,b);
    }

    @CmntGroup(value = "BAMethodGroup")
    public static class B_A {

        //baClass_01
        private String a;

        /**
         * baClass_02
         */
        public void baMethod01(){
            if(true){
                //baMethod_01_01
            }else{
                ///baMethod_01_02
            }
        }
    }

}
