# Assignment 4. Patient Zero

Student ID: 252864
Name: Yerdaulet Yerboluly
Group: SE-2519

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 75
    Shared core: 926
    Marker hits: 5
    First hit: 12734
    Border length: 4
    Shared words: 388
    Tests needed: 35
    Cheapest testing: 149
    Longest chain: 28

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. The cell d[i][j] is the smallest number of edits needed to turn the first i letters of A into the first j letters of B. I take the minimum of deletion from d[i-1][j], insertion from d[i][j-1], and replacement or matching from d[i-1][j-1].

Mission 2. A substring must be one continuous part, but a subsequence can skip letters while keeping their order. Shared core can be larger than the same-position count because insertions and deletions move matching letters to different positions.

Mission 3. The prefix function stores the length of the longest proper prefix that is also a suffix for every marker prefix. On a mismatch KMP moves only the marker pointer using this table, so the text pointer never goes back. My border length is 4: the marker starts and ends with `TGCG`.

Mission 4. Two different strings can have the same hash, so I compare all letters before counting a shared word. If many windows have the same hash, the program performs many extra comparisons and can lose the usual near-linear speed.

Mission 5. For each person I store the best answer when that person is tested and when they are not tested. If the person is not tested, all children must be tested; otherwise each child can use its cheaper state. A highest-degree greedy rule can fail because it ignores testing costs and the choices inside subtrees.

## AI use

I used ChatGPT to explain the algorithm ideas, help draft the implementations, review the code, and find errors. I read every method and checked it with the provided tests and my personal data.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 | 12.8 / 18.4 / 41.9 ms |
| KMP | 20 000 / 40 000 / 80 000 | 3.1 / 1.4 / 1.5 ms |
| edit distance | 500 / 1000 / 2000 / 4000 | 9.8 / 6.6 / 13.2 / 33.2 ms |

The naive search grows with the text size and has a much worse worst case than KMP, while KMP stays close to linear. Edit distance uses a two-dimensional table, so doubling both lengths should approach four times the work. JVM warm-up, garbage collection, and short measurements explain why the measured ratios are not exact.

## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|---|---|
| Edit distance | O(nm) | O(nm) | full DP table |
| LCS | O(nm) | O(nm) | full DP table |
| Prefix function and KMP | O(n + m) | O(m + h) | h is the number of matches returned |
| Rabin-Karp (shared words) | expected O(n), worst O(n^2 k) | O(n) | both strains have length O(n); hash collisions are verified |
| Tree DP | O(v) | O(v) | the graph is a tree |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
