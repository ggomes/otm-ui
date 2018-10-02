/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.utils;

import java.util.HashMap;

public class BijectiveMap<A,B> {

    private HashMap<A,B> AtoB = new HashMap<>();
    private HashMap<B,A> BtoA = new HashMap<>();

    public void put(A a,B b){
        if( !AtoB.containsKey(a) & !BtoA.containsKey(b) ){
            AtoB.put(a,b);
            BtoA.put(b,a);
        }
    }

    public B getFromFirst(A a){
        return AtoB.get(a);
    }

    public A getFromSecond(B b){
        return BtoA.get(b);
    }

}
