# EV Charging Platform & Management System
Tantárgy: A rendszerfejlesztés technológiája és módszertana
Intézmény: Nyíregyházi Egyetem - Matematikai és Informatikai Intézet

1# Pokoraczki Péter - Pokoraczki

2# Hornyák Csaba - hornyakcsaba232

3# Devera Bálint - KISARC

4# Gresnyer József - TrexxxJoc


Projekt struktúra

1. Feladat: Architektúra & Adatbázis (Spring Boot alapok, PostgreSQL, JPA entitások)   
2. Feladat: OCPP & Kommunikáció (WebSocket szerver, JSON üzenetkezelés, BootNotification, Start/Stop Transaction)   
3. Feladat: Üzleti Logika & Hibakezelés (Session Service, orphan session detektálás, State Machine, self-healing)   
4. Feladat: Fizetési Gateway & REST API (REST végpontok, pre-auth zárolás és azonnali void/refund logika)   

#ELSŐ PULL
git clone https://github.com/Pokoraczki/ev-charging.git
git checkout -b dev 
git push origin dev

#PUSH
git add .
git commit -m "szöveg"
git push origin dev

#PULL
git pull origin dev --rebase


#SZÜKSÉGES PLUGINOK/PROGRAMOK
python -m install uvicorn fastapi websockets


postgres
