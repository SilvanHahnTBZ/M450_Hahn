# M450 – Lösungen Übungen 1–3

## Übung 1 – Rabattregeln

**Regeln**
- Preis < 15’000 CHF → 0 %
- 15’000 CHF ≤ Preis ≤ 20’000 CHF → 5 %
- 20’000 CHF < Preis < 25’000 CHF → 7 %
- Preis ≥ 25’000 CHF → 8.5 %

### 1.1 Abstrakte Testfälle

| ID | Bedingung (p in CHF) | Erwarteter Rabatt |
|----|-----------------------|-------------------|
| A1 | p < 15000             | 0 %               |
| A2 | 15000 ≤ p ≤ 20000     | 5 %               |
| A3 | 20000 < p < 25000     | 7 %               |
| A4 | p ≥ 25000             | 8.5 %             |
| G1 | p = 14999.99          | 0 %               |
| G2 | p = 15000             | 5 %               |
| G3 | p = 20000             | 5 %               |
| G4 | p = 20000.01          | 7 %               |
| G5 | p = 24999.99          | 7 %               |
| G6 | p = 25000             | 8.5 %             |

### 1.2 Konkrete Testfälle

| ID  | Kaufpreis (CHF) | Erwarteter Rabatt |
|-----|------------------|-------------------|
| C1  | 14000.00         | 0 %               |
| C2  | 14999.99         | 0 %               |
| C3  | 15000.00         | 5 %               |
| C4  | 17500.00         | 5 %               |
| C5  | 20000.00         | 5 %               |
| C6  | 20000.01         | 7 %               |
| C7  | 22500.00         | 7 %               |
| C8  | 24999.99         | 7 %               |
| C9  | 25000.00         | 8.5 %             |
| C10 | 30000.00         | 8.5 %             |

---

## Übung 2 – Autovermietung SIXT: 5 funktionale Black-Box Tests

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat (laut SIXT Regeln) | Status | Mögliche Ursache |
|----|--------------|---------------------|----------------------------------------|--------|------------------|
| S1 | Suche am Standort Zürich Flughafen mit gültigen Zeiten | Trefferliste mit verfügbaren Fahrzeugen, Pay now und Pay later klar erkennbar, Filter funktionieren | Trefferliste erscheint, Tarifunterscheidung Pay now vs Pay later sichtbar, Filter nutzbar | OK | — |
| S2 | Altersprüfung Fahrer unter 19 | Buchung nicht möglich oder Einschränkung mit Hinweis auf Mindestalter | Schweiz Mindestalter 19, je nach Kategorie höher, unter 19 keine reguläre Buchung | OK | — |
| S3 | Zahlungsmittelregel: Debitkarte für Luxury oder Kartenname ungleich Hauptfahrer | System verlangt Kreditkarte für Sports/Luxury, Karte muss auf einen Fahrer lauten, sonst Abweisung | Debitkarten für Sports und Luxury nicht zugelassen, Name der Karte muss zum Fahrer passen | OK | — |
| S4 | Tarifregeln: Pay now vs Pay later inklusive Storno | Pay later kostenlos stornier und änderbar, Pay now mit Gebühren, Regeln vor Bestätigung sichtbar | Help Center bestätigt kostenlose Änderung bei Pay later und Gebühren bei Prepaid Pay now | OK | — |
| S5 | Buchung ändern bei Pay later | Änderung ohne Gebühr, neue Verfügbarkeit und Preise werden berechnet, Bestätigung aktualisiert | Änderung über Manage booking möglich, ohne Gebühr bei Pay later, Preise werden neu berechnet | OK | — |

**Hinweis:** Bei Pay later wird das Fahrzeug bis 60 Minuten nach der geplanten Abholzeit innerhalb der Öffnungszeiten gehalten.

---

## Übung 3 – Simple Bank Software

### 3.1 Mögliche Black-Box Testfälle

| ID | Beschreibung | Erwartetes Resultat |
|----|--------------|---------------------|
| B1 | Konto anlegen mit gültigen Daten | Konto mit eindeutiger ID wird erstellt |
| B2 | Einzahlung mit positivem Betrag | Kontostand erhöht sich um den Betrag |
| B3 | Abhebung mit ausreichender Deckung | Kontostand verringert sich, Vorgang wird protokolliert |
| B4 | Abhebung mit unzureichender Deckung | Ablehnung mit Fehlermeldung, Kontostand unverändert |
| B5 | Überweisung von Konto A nach B | Betrag wird bei A abgezogen und bei B gutgeschrieben, atomar |
| B6 | Eingaben 0, negativ, sehr groß | 0 und negativ werden abgewiesen, große Beträge korrekt verarbeitet |
| B7 | JSON Roundtrip Account zu JSON und zurück | Keine Informationsverluste nach Serialisierung und Deserialisierung |
| B8 | API Aufruf falls vorhanden | Erfolgreiche Antwort, Fehlerfälle mit sinnvollen Meldungen |

### 3.2 Kandidaten für White-Box Tests (Methoden)

- `Account.deposit(amount)`
- `Account.withdraw(amount)`
- `Bank.transfer(from, to, amount)`
- Repository oder `Bank.findAccount(id)`
- HTTP Client mit OkHttp: Fehlercodes, Timeouts, Retry
- JSON Serializer und Deserializer mit Gson

### 3.3 Code-Verbesserungen und Best Practices

- Geldbeträge mit `BigDecimal`, feste Scale und `RoundingMode`
- Eingabevalidierung: keine 0 oder negativen Beträge, sinnvolle Obergrenzen
- Überweisungen atomar, keine Partialupdates
- Schichten sauber trennen: Domäne, Service, Persistenz, API
- Einheitliches Exception Handling und Logging
- Abhängigkeiten injizieren für testbare Komponenten
- JSON Felder explizit benennen, Null Handling klar definieren
- Konfiguration über Properties oder Umgebungsvariablen, keine Secrets im Code
