# M450 – Grundlagen zu Testing und Testing in Vorgehensmodellen

Diese Abgabe enthält kurze, prüfbare Antworten zu Aufgabe 1 und 2 sowie die vollständige Implementierung von Aufgabe 3 (inkl. Testtreiber) und die Bonus-Korrektur.

---

## Lernziele (Kurzfassung)

- Gründe für das Testen nennen (Qualität, Zuverlässigkeit, Risikoreduktion)
- Fehler vs. Mangel unterscheiden
- Fehlermaskierung erklären
- Warum vollständiges Testen nicht möglich ist (kombinatorische Explosion)
- Kriterien für gute Testfälle (Wahrscheinlichkeit, Unabhängigkeit, geringe Redundanz, Abdeckung)
- Vorgehensmodelle (V-Modell, SCRUM) und typische Testarten zuordnen

---

## Repository-Struktur

    M450_Hahn/
    └─ testing-basics/
       ├─ README.md
       ├─ .gitignore
       └─ src/
          ├─ PriceCalculator.java
          └─ PriceCalculatorTestDriver.java

Die `.java`-Dateien liegen bewusst flach in `src/` (kein Maven/Gradle-Layout), damit die Abgabe kompakt bleibt.

---

## Aufgabe 1 – Testarten (Was? Wie in der Praxis?)

- **Unit-/Komponententest**  
  Test einzelner Funktionen/Klassen isoliert.  
  Wie: automatisiert (z. B. JUnit), Mocks/Stubs für Abhängigkeiten, in CI/CD integriert.

- **Integrationstest**  
  Zusammenspiel mehrerer Komponenten/Services.  
  Wie: mit echter Test-DB oder Testcontainers, API-Aufrufe, Schnittstellen und Datenfluss prüfen.

- **System-/End-to-End-Test**  
  Gesamtsystem aus Benutzersicht.  
  Wie: UI-/API-Flows (z. B. Playwright/Cypress/Postman), realistische Testdaten in Testumgebung.

(Optional: Abnahmetest/UAT, Regressionstest, Smoke-/Sanity-Test.)

---

## Aufgabe 2 – Beispiele

- **SW-Fehler (Bug):** Rabattlogik gewichtet „≥ 5 Extras“ fälschlich nur mit 10 % statt 15 % (falsche if-Reihenfolge).
- **SW-Mangel:** Berechnung korrekt, aber GUI zeigt Preis in falscher Währung oder rundet falsch → Darstellungs-/Usability-Problem.
- **Beispiel hoher Schaden:** Medizintechnik dosiert Medikamente falsch; Banking-Software bucht falsche Beträge; sicherheitskritische Fahrzeugsteuerung (ABS/ESP) reagiert fehlerhaft.

---

## Aufgabe 3 – Preisberechnung und Testtreiber

**Fachlogik (gemäss Aufgabenstellung):**
- `baseprice` mit **Händlerrabatt `discount` (nur auf baseprice)**
- `+ specialprice` (Sondermodellaufschlag)
- `+ extraprice` (Zubehör) mit **Zubehör-Rabatt nur auf extraprice**
  - ab 3 Extras: 10 %
  - ab 5 Extras: 15 %

**Implementierung:** `src/PriceCalculator.java`  
**Testtreiber:** `src/PriceCalculatorTestDriver.java` (konsolenbasiert, ohne JUnit)

### Rechenformel (wie implementiert)

    baseAfterDealer  = baseprice  * (100 - discount) / 100
    extrasAfterAddon = extraprice * (100 - addon_discount) / 100
    total            = baseAfterDealer + specialprice + extrasAfterAddon

### Abgedeckte Testfälle (Ausschnitt)

| Fall | Eingaben (base, special, extra, extras, discount) | Erwartet |
|-----:|----------------------------------------------------|---------:|
| 1 | 10000, 500, 0, 0, 0 | 10500 |
| 2 | 10000, 500, 2000, 2, 5 | 12000 |
| 3 | 10000, 500, 3000, 3, 0 | 13200 |
| 4 | 10000, 1000, 4000, 5, 5 | 13900 |

Der Testtreiber gibt pro Fall OK/FAIL aus und endet mit `ALL TESTS PASSED` bei Erfolg.

---

## Build & Run

PowerShell (empfohlen unter Windows):

    cd .\testing-basics\
    mkdir out -Force
    javac -d out .\src\PriceCalculator.java .\src\PriceCalculatorTestDriver.java
    java -cp out PriceCalculatorTestDriver

Alternative (cmd.exe mit Wildcard):

    cd testing-basics
    mkdir out
    javac -d out src\*.java
    java -cp out PriceCalculatorTestDriver

Hinweis: In PowerShell werden Wildcards bei `javac` nicht immer wie erwartet expandiert; daher oben explizit beide Dateien angegeben.

---

## Bonus – Gefundene Fehler und Korrektur

1. **Bedingungsreihenfolge in der Zubehör-Rabattlogik**  
   Falsch: `if (extras >= 3) … else if (extras >= 5) …` → der `>=5`-Zweig ist unerreichbar.  
   Richtig: zuerst `>= 5`, dann `>= 3`, sonst `0`.

2. **Vermischung der Rabatte**  
   Falsch: `if (discount > addon_discount) addon_discount = discount;` (Händler-Rabatt überschreibt Zubehör-Rabatt).  
   Richtig: Rabatte getrennt anwenden (discount nur auf `baseprice`, addon_discount nur auf `extraprice`).

Beides ist in der Implementierung berücksichtigt.

---
## Build & Run (PowerShell)

```powershell
cd .\M450_Hahn\testing-basics
mkdir out -Force
javac -d out .\src\PriceCalculator.java .\src\PriceCalculatorTestDriver.java
java -cp out PriceCalculatorTestDriver
```
