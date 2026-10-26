# CA1 – Algorithms and Constructs: Fast-Food Party Storage

Trays are added through the **front side** of the unit and removed from the **opposite side**,
  so the first tray placed is the first one removed – **First In, First Out (FIFO)**.
  This follows the food-safety FIFO rule used in kitchens: the oldest food (closest to its
  best-before date) always leaves first.

  **Chosen data structure:** a **Queue** implemented with a **circular array** of capacity 8.

---

## 📅 Development timeline

| #   | Date       | Step               | What was done                                                                                                     |
| --- | ---------- | ------------------ | ----------------------------------------------------------------------------------------------------------------- |
| 1   | 06/10/2026 | Repository created | Initial commit on GitHub                                                                                          |
| 2   | 26/10/2026 | Reference material | Added sample posters provided for the assignment (`poster_examples/`)                                             |
| 3   | 26/10/2026 | Maven project      | Created the project with VS Code (`maven-archetype-quickstart`) in `food-storage/`                                |
| 4   | 26/10/2026 | `.gitignore`       | Ignored build output (`target/`, `*.class`) and IDE files                                                         |
| 5   | 26/10/2026 | Java 17            | Updated `pom.xml` from Java 1.7 to 17 (1.7 is not supported by current JDKs and has no `java.time` API for dates) |
| 6   | 26/10/2026 | Class structure    | Created the skeleton of all classes (see below) and removed the archetype sample files                            |
| 7   | _next_     | README             | Organised this README with the project timeline                                                                   |
| 8 | 26/10/2026 | FoodType | Enum with the five foods, display names and parsing of user input (number or name) |
| 9 | 26/10/2026 | FoodItem | Constructor, validation (weight 1-5000 g, best-before ≤ 14 days), getters and toString |

### 🔜 Next steps
- [x] `FoodType` – display names and parsing user input
- [x] `FoodItem` – constructor, validation (weight, best-before ≤ 14 days), getters, `toString`
- [ ] `FoodStorage` – `addFront`, `isEmpty`, `isFull`
- [ ] `FoodStorage` – `removeFront` (LIFO) and `removeRear` (FIFO)
- [ ] `FoodStorage` – `peek`, display, `search`
- [ ] `FoodStorageApp` – menu with input validation and error handling
- [ ] JUnit 5 tests
- [ ] Time-complexity analysis
- [ ] Poster (PPT) and video

---

## 👥 Team 

- Andre Luiz Oliveira da Cunha - 2024214
- Brenda Lima Araujo - 2024264


## 🤖 Use of Generative AI
TODO

## 📚 References
TODO
