# KoprVelkyHlad

##2. Projekt##

Startup velky.hlad.sk chce postaviť aplikáciu na správu kuchárskych receptov. Usúdila, že na tento účel je najlepšie postaviť webovú službu.

Kuchársky recept obsahuje:

* názov receptu
* autora
* zoznam ingrediencí vrátane merných jednotiek (napr. 4 vajcia, 400 g hladkej múky, 1 lyžica cukru)
* postup prípravy. 

Implementujte nasledovné operácie:

   *  pridanie receptu do databázy receptov. Návratovou hodnotou je jednoznačný UUID identifikátor receptu. Vstupom je recept s vyššie uvedenými vlastnosťami.
   *  aktualizácia receptu na základe identifikátora
   *  odstránenie receptu na základe identifikátora
   *  získanie receptu na základe identifikátora
   *  vyhľadanie receptov obsahujúcich zadané kľúčové slovo či slová. V tejto funkcionalite implementujte fulltextové vyhľadávanie, pričom algoritmus môže byť aj veľmi jednoduchý.
   *  vyhľadávanie receptov podľa ingrediencií. Pokryte situácie, keď máte doma napr. vajcia, múku a bravčové mäso a chcete vedieť, aké recepty si môžete pripraviť. 

Pri všetkých operáciách ošetrite situácie, keď sa recept v databáze nenachádza, a to rozumným spôsobom, ktorý klienta dostatočne oboznámi s chybovým stavom.

Implementáciu databázy zvoľte podľa vlastného uváženia. Nezabúdajte na to, že ku databáze receptov budú pristupovať viacerí klienti naraz.
Zvoľte si aspoň jednu z nasledovných možností (REST/SOAP).

###I. SOAP webservice###

Implementujte webovú službu s vyššie uvedenými operáciami. Služba nech je realizovaná v štýle document/literal alebo RPC/encoded. 
Zverejnite WSDL so službou a vytvorte k nej klienta.
Na implementáciu servera i klienta použite ľubovoľnú technológiu.
Demonštrujte funkčnosť servera i klienta -- odporúčaná forma sú unit testy.

###II. REST webservice###
Navrhnite a implementujte REST API pre vyššie uvedené operácie na strane servera. Použite ľubovoľný vhodný framework: odporúčaný je Spring MVC (Spring Boot) alebo Restlet.
Implementujte klienta pre uvedenú webovú službu v odlišnej technológii / programovacom jazyku. Ak ste implementovali server v Jave, 
ste povinní implementovať klienta v odlišnom programovacom jazyku: napr. PHP / Python / Pascal a pod.
Demonštrujte funkčnosť servera i klienta -- odporúčaná forma sú unit testy.

Vyjasnenia

- v prípade voľby variantu I. SOAP môžete i klienta i server implementovať v rovnakej technológii na rovnakej platforme (JAX-WS server + JAX-WS klient).
- v prípade voľby variantu II. REST musíte klientskú stranu implementovať v odlišnej technológii než server. 