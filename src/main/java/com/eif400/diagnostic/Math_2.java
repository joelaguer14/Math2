/**
* Demo
* Solucion al ejercicio 4 del Diagnostico I-2020
* Requiere jdk14 --enable-preview 
* Ejemplifica FP-OOP
* Copyright: NO distribuya sin permiso explicito del autor
* Para compilar de consola: Asumiendo Math.java en src
* javac  -d classes --enable-preview  --release 14 -nowarn src\Math.java
* Da warnings del preview pero se pueden ignorar
* Para correr:
* java --enable-preview -cp classes com.eif400.diagnostic.Math

@author loriacarlos@gmail.com
@since 2020

*/
package com.eif400.diagnostic;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;


public class Math_2 {
   /**
   * Modelo de estado inmutable explicito en un objeto
   */
   record EState(int i, double s, double t){
       public static EState initial(){
           return new EState(0, 0, 1);
       }
       public EState update(){
           return new EState( i + 1, s + t,  t / (i + 1) );
       }
       public boolean isFinal(int n, double epsilon){
           return i > n || t < epsilon;
       }
       
   }
   // Constantes para el demo
   static final double PRECISION   = 1e-15;
   static final int MAX_ITERATIONS = 20;
   static final double E = java.lang.Math.E;
   //////////////////////////////////////////////////////////////////////////
   /**
   * Version FP estado explicito e inmutable
   */
   public static double aproximeE_FP(){
       return aproximeE_FP(MAX_ITERATIONS, PRECISION);
   }
   public static double aproximeE_FP(int n){
       return aproximeE_FP(n, PRECISION);
   }
   public static double aproximeE_FP(int n, double epsilon){
       return    Stream.iterate( EState.initial(), EState::update  )
                       .filter( state -> state.isFinal( n, epsilon ) )
                       .findFirst()
                       .get()
                       .s();
   }
   ////////////////////////////////////////////////////////////////////////////
   /**
   * Version recursiva de cola, estado explicito e  inmutable
   */
   private static double aproximeE_TAILREC(EState state, int n , double epsilon){
       return ( state.i() > n || state.t() < epsilon ) 
              ? state.s() 
              : aproximeE_TAILREC(state.update(), n , epsilon);
   }
   public static double aproximeE_REC(int n, double epsilon){
       return aproximeE_TAILREC(EState.initial(), n, epsilon);
   }
    public static double aproximeE_REC(int n){
        return aproximeE_TAILREC(EState.initial(), n, PRECISION);
    }
   ///////////////////////////////////////////////////////////////////////////
   /**
   * Version iterativa clasica (estado mutable, estado no explicito en objeto)
   */
   public static double aproximeE_IMPER(int n){
       double s = 0;
       double t = 1;
       
       for (int i = 0; i <= n; i++){
           s += t;
           t = t / ( i + 1 );
           
       }
       return s;
   }
   
   public static void main(String[] args){
       System.out.println("*** Testing aproximeE ITER, REC, FP ***");
       for (int n =10; n < 25; n++){
           Long tiempoFPInicio=System.currentTimeMillis();
           Double fP=aproximeE_FP(n);
           Long tiempoFPFinal=System.currentTimeMillis();
           Long tiempoFP=tiempoFPFinal-tiempoFPInicio;

           Long tiempoFPConcurrenteInicio=System.currentTimeMillis();
           Double fPConcurrente=aproximeE_FP(n);
           Long tiempoFPConcurrenteFinal=System.currentTimeMillis();
           Long tiempoFPConcurrente=tiempoFPConcurrenteFinal-tiempoFPConcurrenteInicio;
         System.out.format(
                 "n=%2d Math.E=%1.16f, E_FP=%1.16f Tiempo: %d  E_FPConcurrente=%1.16f Tiempo: %d  %n",
                 n,
                 E,
                 fP, tiempoFP,fPConcurrente,tiempoFPConcurrente);


       }
       
   }
}
