## Unit and Integration Test Coverage Results
|   | Class | Method | Line | Branch |
|---|-------|--------|------|--------|
|CalculationService | 100% | 100% | 100% | 90% |
|PathFindingService | 100% | 100% | 96% | 90% |

## Correctness Testing Results (Requirement (i))

### Systematic Tests 
| Test Case                         | Result |
|-----------------------------------|--------|
| Lagre NFZ blocking between points | PASS   |
| Many large NFZ between points | PASS |
| Destination inside NFZ            | PASS   |
| Origin surrounded by NFZ          | PASS   |
| Destination surrounded by NFZ | PASS |
| Origin and destination suroruned by NFZ | PASS |

### Random Tests 
| NFZ Size | Close Pass/Fail | Far Pass/Fail |
|----------|-----------------|---------------|
| 0        | 1 /      0      | 1 /     0     |
| 10       | 50 /      0     | 50 /     0    |
| 20       | 50 /      0     | 50 /     0    |
| 30       | 50 /      0     | 50 /     0    |
| 40       | 50 /      0     | 50 /     0    |
| 50       | 50 /      0     | 50 /     0    |
| 60       | 50 /      0     | 50 /     0    |
| 70       | 50 /      0     | 50 /     0    |
| 80       | 50 /      0     | 50 /     0    |
| 90       | 50 /      0     | 50 /     0    |
| 100      | 50 /      0     | 50 /     0    |

## Performance Testing Results (Requirement (ii))

### Random Tests - Requirement (ii)
| NFZ Size | Close Avg (ms) | Far Avg (ms) |
|----------|----------------|--------------|
| 0        | 3.00           | 29.00        |
| 10       | 3.86           | 36.50        |
| 20       | 4.74           | 49.14        |
| 30       | 6.10           | 84.46        |
| 40       | 11.66          | 79.96        |
| 50       | 9.24           | 157.76       |
| 60       | 11.32          | 134.22       |
| 70       | 13.04          | 131.12       |
| 80       | 35.10          | 152.92       |
| 90       | 97.48          | 194.06       |
| 100      | 24.90          | 261.16       |

## Mutation Testing Results

- Line Coverage (for mutated classes only): 55/58 (95%)
- Generated 11 mutations Killed 11 (100%)
- Mutations with no coverage 0. Test strength 100%
- Ran 19 tests (1.73 tests per mutation)
- Pit test coverage report can be viewed [here](https://kkaiying.github.io/Cw1/mutation-testing/index.html)