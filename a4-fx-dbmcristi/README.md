Aplicatie pentru gestionarea masinilor.
  - CRUD
  - Cele mai des închiriate mașini.Se afiseaza datele pentru fiecare mașină precum și numărul de închirieri, în ordine descrescătoare a numărului de închirieri.
  - Numărul de închirieri efectuate în fiecare lună a anului.
  - Mașinile care au fost închiriate cel mai mult timp. Se afiseaza datele pentru fiecare mașină precum și numărul total de zile de închiriere pentru fiecare. 

- Repository permite stocarea entităților din domeniu într-o bază de date SQL. 
- Tipul de repository este utilizat(in memory, binary, file, database) se va face prin folosirea fișierului *settings.properties*
- Implementarea ce utilizează repository SQL va avea 100 entități generate pseudo-aleator și salvate în baza de date. 
- Implementarea interfeti grafice cu JavaFx.
- Implementarea cu ajutorul Java 8 streams 

- Setare în fișierul *setttings.properties*, în care se va specifica dacă aplicația pornește în linie de comandă sau prin interfață grafică.
 Modificarea acestei setări în fișier determină modificarea modului de pornire al aplicației, fără a fi necesare modificări la nivelul codului sursă. 
