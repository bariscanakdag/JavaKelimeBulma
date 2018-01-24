package com.bariscanakdag;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Math.pow;

public class Main {

    public static void main(String[] args)  {
	//dosya okuma C:\Users\asus\Desktop\DERSLER\JavaDerslerı\BarisCanAkdag\dosya.txt
        int diziKapasite=47;
        String[] kelimeDizi = new String[diziKapasite];
        String  okunanVeri;
         int  hashKapasite=127;
         String arananKelime;

        int secim=1;


        int [] asciiDizi=new int[diziKapasite];
        HashMap<Integer,String> table = new HashMap<>(hashKapasite);  //hash kapasitesi belirlenir.


        try {                                                                   //dosya okuma işlemleri
            FileInputStream fstream = new FileInputStream("Dosya.txt");
            DataInputStream dstream = new DataInputStream(fstream);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(dstream));
            for (int i = 0; i < diziKapasite; i++) {

                okunanVeri = bReader.readLine();
                kelimeDizi[i] = okunanVeri; //okunanan veri kelime dizisine atılır.


            }
            dstream.close();  // dosya kapatılır.
        } catch (Exception e) {
            System.err.println("Fazla Basıyor");
        }
        System.out.println("--------Kelime Dizisi --------"); //kelime dizisinde bulunan kelimeler ekrana basılır.
        for(int i=0;i<diziKapasite;i++){
            if(kelimeDizi[i]!=null){
                System.out.println(i+".ci kelime : "+kelimeDizi[i]);
            }

        }

       AsciiDizi(kelimeDizi,asciiDizi,diziKapasite); // kelimeler asciiye ayrılıp diziye aktarılır.

        System.out.println("-----ascii dizi-----"); //ascii dizisinin içeriği ekrana bastırılır.
        for(int i=0;i<asciiDizi.length;i++){
            System.out.println(i+". satır :"+asciiDizi[i]);
        }

        HashYerlestirme(asciiDizi,kelimeDizi,diziKapasite,table,hashKapasite); // ascidizisindeki kelimeler hash tablosuna quadratic yerleştirilir.

        System.out.println("---------hash table içi------------ "); //hash tablenin içeriği ekrana bastırılır.
        for(int i=0;i<hashKapasite;i++){
            if(table.get(i)!=null)
               System.out.println(i+"."+"eleman :"+table.get(i));
        }


        while(secim==1)                // programın  yeniden çalışmasını sağlar.İstenilirse bitirlir.
        {
            int sayi=0;

            System.out.println("Aramak istediginiz kelimeyi giriniz : "); //aranacak kelime alınır.
        Scanner tara = new Scanner(System.in);
        arananKelime=tara.next();
        System.out.println("------Normal Aranıyor...-------"); //kelime olduğu gibi aranır.Var veya yokise ekrana basılır.
                normalArama(arananKelime,table,hashKapasite);
            int kontrol=    normalAramaKontrol(arananKelime,table,hashKapasite,sayi); // Kelime var ise program diğer aramaları atlar.Kelime yoksa ise harf çıkartarak ve yer değiştirerek arar.
            if(kontrol==0){
                System.out.println("----Harf Çıkartılarak Aranıyor------");  // aranan kelime yok ise tek tek harf çıkartarak var mı diye bakar.
                harfCikartmaliArama(arananKelime,table,hashKapasite);

                System.out.println("----------Yer Değiştirilerek Aranıyor-------");  //aranan kelime yok ise harflerin yerini değiştirerek arar.
                YerDegistirmeliArama(arananKelime,table,hashKapasite);
            }






            System.out.println("Tekrar arama yapmak için 1 e çıkmak için 0 a basınız! :"); // Devam etmek için değer bekler 1 devam eder.Diğer rakamlar programı bitirir.
            secim=tara.nextInt();
        }
        System.out.println("--------SON-------");
        System.out.println("--------PROGRAM BİTMİŞTİR---------");

    }

    private static int normalAramaKontrol(String arananKelime, HashMap<Integer, String> table, int hashKapasite, int sayi) { // normal arama kontrolu.

        int toplam=arananKelimeAscii(arananKelime);  //aranan kelime asciiye çevirilmek üzere arananKelimeAscii metoduna yollanır .Gelen değer toplama atılır.
        int key;
        for (int i = 0; i <hashKapasite; i++) {  //tüm hashı dolaşması için hash kapasitesine kadar koşturulur.

            key = (int) ((toplam + pow(i, 2)) % hashKapasite);  //quadratic olarak aramamız sağlanır.

            if (arananKelime.equals(table.get(key))==true){                  // hashın dolu olduğunda durumda kelimeler aynımı diye karşılaştırılır
                sayi++;                                                      //kelimeler aynı ise kontrolumuzu 1 arttır.
                break;                                                         //döngüden çıkar

            }


            if(table.containsKey(key)==false ){    //  Hash boş olduğu durumda   kelime kesin  yoktur.


                sayi=0;                             //kelime olmadığı durumda kontrolumuzu sıfırlar.
                break;
            }


        }
        return sayi; // kontrolumuz main motuduna geri gönderilir.Eğer sıfır ise kelime yoktur .Harf çıkarma ve değiştirme metodunu çalıştırır.Sayımız 0 dan farklı ise diğer metotlara girmeden program bitilir.
    }


    private static void YerDegistirmeliArama(String arananKelime, HashMap<Integer, String> table, int hashKapasite) { //bu metot yer değiştirme algoritmasını kullanarak kelimenin harflerini değiştirir.



        char[] chararananKelime; // char dizi oluşturulur.Kelimeyi harflere bölmek için.

        chararananKelime=arananKelime.toCharArray();     // String -> char dönüşümü yapılır.Kelime harfleri char diziye aktarılır.


        char temp;   // gecici tanımlanır.

        try {
            for(int i=0;i<arananKelime.length()-1;i++){ // harf sayının 1 eksiği kadar  çalışır.

                temp=chararananKelime[i];                   // 0 ile 1 sonra 1 ile 2 sonra 2 ile 3 yer değiştirirlir.Kelime uzunluğunun 1 eksiğine kadar.
                chararananKelime[i]=chararananKelime[i+1];
                chararananKelime[i+1]=temp;
                arananKelime=String.valueOf(chararananKelime);  // char -> String dönuşumu yapılır.



                normalArama(arananKelime,table,hashKapasite); //harfleri değiştirilen yeni kelime aramaya yollanır.

                temp=chararananKelime[i];                   //Harfler eski haline çevirilir.
                chararananKelime[i]=chararananKelime[i+1];
                chararananKelime[i+1]=temp;
                arananKelime=String.valueOf(chararananKelime);

            }

        }catch (Exception e) {
            System.out.println(e.getMessage());


        }
    }


    private static void harfCikartmaliArama(String arananKelime, HashMap<Integer, String> table, int hashKapasite) { //Aranan kelimedeki harfleri tek tek silerek aramaya yollar.



        StringBuffer YeniKelime = new StringBuffer();

        for(int i=0;i <arananKelime.length();i++){
            YeniKelime.append(arananKelime);                       // StringBuffer  String dönüşümü yapılır.
            YeniKelime.deleteCharAt(i);                            // harf silme işlemi yapılır.

            normalArama(YeniKelime.toString(),table, hashKapasite); // aramaya yollanır.
            YeniKelime.delete(0,arananKelime.length());          // StringBuffer değerimizi siliyoruz.(Yenisi almak için)

        }
    }


    private static void normalArama(String arananKelime, HashMap<Integer, String> table, int hashKapasite) {

        int toplam=arananKelimeAscii(arananKelime);         //aranan kelime asciiye ayrılmak üzere aranakelimeascii metoduna yollanır.
        int key;
        for (int i = 0; i <hashKapasite; i++) {             //tüm hash gezmek için hash kapasitesine kadar koşturulur.

            key = (int) ((toplam + pow(i, 2)) % hashKapasite);  // quadratic arama için .

            if (arananKelime.equals(table.get(key))==true){                  // hash dolu ise kelimeler aynımı diye karşılaştırılır.
                System.out.println("kelime bulundu:" + "  " + arananKelime); //kelime bulunursa ekrana basar ve döngüden çıkar.

                break;

            }


            if(table.containsKey(key)==false ){    //  Hash boş ise kelime yoktur.

                    System.out.println("kelime bulunamadı :"); //ekrana basar ve çıkar.

                break;
            }


        }
    }

    private static int arananKelimeAscii(String arananKelime) { // gelen kelimenin asciisini bulur.
        int toplam=0;
        char harf;
        int harfAscii;
        for(int j=0;j<arananKelime.length();j++){  // Kelimenin harflerini asciiye çevirip tolar.
            harf=arananKelime.charAt(j);
            harfAscii=(int) harf;
            toplam=toplam + harfAscii*(j+1);

        }
        return  toplam;   //kelimenin ascii değerini geri döndürür.


    }

    private static void HashYerlestirme(int[] asciiDizi, String[] kelimeDizi, int kelimeSayisi, HashMap<Integer, String> table, int hashKapasite) {//Hash tablosuna quadratic olarak yerleştirir.


        int key;

        for(int j=0; j<kelimeSayisi;j++) { // Asciidizisinde bulunan kelime sayısına kadar koşturur.

            for (int i = 0; i <hashKapasite; i++) {                                 // hash kapasitine kadar koşturur.
                key = (int) ((asciiDizi[j] + pow(i, 2)) % hashKapasite);            //quadratic olarak sıralama için fonksiyonumuz.
                if (table.containsKey(key) == false) {                             // Hashin boş olup olmadığına bakar boş  ise yerleştirir.
                    table.put(key, kelimeDizi[j]);
                    break;                                                           //Yerleştirdiği zaman içteki foru kırar.

                }
            }
        }
    }


    private static void AsciiDizi(String[] kelimeDizi, int[] asciiDizi,int kapasite) { //Kelimedizisindeki kelimelerin ascii karşılığı halini Asciidizisine atar.
        char harf;
        int harfAsciiDeger;

            for(int i = 0;i< kapasite;i++){ //kelime dizisinin kapasitine kadar koşar
                String geciciKelime =kelimeDizi[i]; //kelime dizisindeki kelimeleri alır.
                int toplam=0;

                for(int j=0;j<geciciKelime.length();j++){       //Asciiye çevirme işlemlerini yapar.
                    harf=geciciKelime.charAt(j);
                    harfAsciiDeger=(int)harf;
                    toplam=toplam+harfAsciiDeger*(j+1);


                }
                asciiDizi[i]=toplam; //kelimenin ascii karşılığını ascii diziye atar.
            }
    }


}

