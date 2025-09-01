# M450 – Lösungen Übungen 1–3

## Übung 1 – Rabattregeln

**Regeln (aus Spezifikation):**
- Preis < 15’000 CHF → 0 %
- 15’000 CHF ≤ Preis ≤ 20’000 CHF → 5 %
- 20’000 CHF < Preis < 25’000 CHF → 7 %
- Preis ≥ 25’000 CHF → 8.5 %

### 1.1 Abstrakte Testfälle (Äquivalenzklassen & Grenzen)

| ID | Bedingung (p in CHF)         | Erwarteter Rabatt |
|----|------------------------------|-------------------|
| A1 | p < 15000                    | 0 %               |
| A2 | 15000 ≤ p ≤ 20000            | 5 %               |
| A3 | 20000 < p < 25000            | 7 %               |
| A4 | p ≥ 25000                    | 8.5 %             |
| G1 | p = 14999.99                 | 0 %               |
| G2 | p = 15000                    | 5 %               |
| G3 | p = 20000                    | 5 %               |
| G4 | p = 20000.01                 | 7 %               |
| G5 | p = 24999.99                 | 7 %               |
| G6 | p = 25000                    | 8.5 %             |
| R1 | p = 0                        | 0 % oder Fehler*  |
| R2 | p < 0                        | Fehler*           |
| R3 | p = NaN / leer               | Fehler*           |

\* Robustheit je nach Spezifikation: Eingaben validieren und ablehnen.

### 1.2 Konkrete Testfälle

| ID  | Kaufpreis (CHF) | Erwarteter Rabatt |
|-----|-----------------|-------------------|
| C1  | 14000.00        | 0 %               |
| C2  | 14999.99        | 0 %               |
| C3  | 15000.00        | 5 %               |
| C4  | 17500.00        | 5 %               |
| C5  | 20000.00        | 5 %               |
| C6  | 20000.01        | 7 %               |
| C7  | 22500.00        | 7 %               |
| C8  | 24999.99        | 7 %               |
| C9  | 25000.00        | 8.5 %             |
| C10 | 30000.00        | 8.5 %             |
| C11 | 0.00            | 0 % oder Fehler*  |
| C12 | -1.00           | Fehler*           |

---

## Übung 2 – Autovermietung: 5 funktionale Black-Box-Tests

| ID | Beschreibung | Erwartetes Resultat | Effektives Resultat | Status | Mögliche Ursache |
|----|--------------|---------------------|---------------------|--------|------------------|
| R1 | Suche an Standort Zürich Flughafen mit gültigem Zeitraum | Trefferliste mit verfügbaren Fahrzeugen; Filter anwendbar; Preise sichtbar | Trefferliste mit Fahrzeugkategorien wird angezeigt, Filter und Preisangaben funktionieren | OK | — |
| R2 | Fahrer unter Mindestalter (z. B. 18 Jahre) versucht zu buchen | Buchung abgelehnt mit klarer Alters-Hinweismeldung | System zeigt Hinweis „Mindestalter 19 Jahre“ und verhindert Abschluss | OK | Altersregel korrekt implementiert |
| R3 | Zahlungsmittel-Validierung: Debitkarte für Luxusklasse | Deutliche Fehlermeldung bzw. Einschränkung; Auswahl nicht möglich | System verlangt Kreditkarte, Debitkarte wird abgelehnt | OK | Zahlungsregel erzwungen |
| R4 | Stornieren/Ändern einer Pay-later-Buchung | Änderung/Storno ohne Gebühr möglich, neue Preise werden berechnet | Änderung im Kundenkonto möglich, Preise werden neu berechnet, keine Gebühren | OK | Tariflogik korrekt umgesetzt |
| R5 | Abholung ausserhalb Öffnungszeiten | System verhindert Buchung oder zeigt Out-of-hours-Option mit Hinweis | Bei geschlossener Station erscheint Meldung „Abholung nicht möglich“; bei Stationen mit 24h-Service wird Option mit Zusatzgebühr angezeigt | OK | Öffnungszeiten/Policies greifen |


## Übung 3 – Simple Bank Software

### 3.1 Mögliche Black-Box-Testfälle

| ID | Beschreibung | Erwartetes Resultat |
|----|--------------|---------------------|
| B1 | Konto anlegen (gültige Daten) | Konto mit eindeutiger ID wird erstellt |
| B2 | Einzahlung (positiver Betrag) | Saldo steigt um Betrag |
| B3 | Abhebung (ausreichende Deckung) | Saldo sinkt; Vorgang protokolliert |
| B4 | Abhebung (unzureichende Deckung) | Ablehnung; Saldo unverändert |
| B5 | Überweisung A→B (beide existieren) | Betrag bei A−, bei B+; atomar |
| B6 | Ungültige Beträge (0, negativ, extrem groß) | 0/negativ abgewiesen; große zulässige Beträge korrekt |
| B7 | JSON-Roundtrip (Account → JSON → Account) | Keine Informationsverluste |
| B8 | Netzwerkfehler beim API-Call | Sinnvolle Fehlermeldung/Retry/Timeout |

### 3.2 White-Box-Kandidaten (Methoden/Pfade mit Links)

- [`Account.deposit(double amount)`](./src/main/java/ch/tbz/bank/software/Account.java) – Normalfall, `amount <= 0` Fehlerpfad
- [`Account.withdraw(double amount)`](./src/main/java/ch/tbz/bank/software/Account.java) – Deckung vorhanden / nicht vorhanden
- [`Bank.createAccount(String, Currency, double)`](./src/main/java/ch/tbz/bank/software/Bank.java) – korrektes Anlegen von Konten
- [`Bank.getAccount(int nr)`](./src/main/java/ch/tbz/bank/software/Bank.java) – Treffer / nicht vorhanden
- [`Bank.deleteAccount(Account)`](./src/main/java/ch/tbz/bank/software/Bank.java) – Konto existiert / existiert nicht
- [`Counter.transferAmount(Account, Account)`](./src/main/java/ch/tbz/bank/software/Counter.java) – Erfolg, Konto existiert nicht, Konto identisch
- [`ExchangeRateOkhttp.getExchangeRate(String, String)`](./src/main/java/ch/tbz/bank/software/ExchangeRateOkhttp.java) – Erfolgsfall, Fehlerfall (Timeout, API down)
- [`Main.main()`](./src/main/java/ch/tbz/bank/software/Main.java) – Start des Programms, Menü-Navigation

### 3.3 Verbesserungen / Best Practices

- Geldbeträge mit `BigDecimal` (feste `scale`, `RoundingMode`)
- Eingabe-Validierung & aussagekräftige Exceptions
- Transaktionen atomar (keine Partial-Updates)
- Schichten trennen: Domain / Service / Persistence / API
- Dependency Injection für testbare Komponenten
- Einheitliches Logging & Fehlerbehandlung
- Klare JSON-Schemas, Null-Handling definiert
- Konfiguration via Properties/Env; keine Secrets im Code
