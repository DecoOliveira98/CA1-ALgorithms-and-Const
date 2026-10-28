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
| 7   | 26/10/2026 | README             | Organised this README with the project timeline                                                                   |
| 8   | 26/10/2026 | FIFO decision      | Chose a FIFO Queue on a circular array (removed RemovalMode)                                                      |
| 9   | 26/10/2026 | FoodType           | Enum with the five foods, display names and parsing of user input (number or name)                                |
| 10  | 26/10/2026 | FoodItem           | Constructor, validation (weight 1-5000 g, best-before ≤ 14 days), getters and toString                            |
| 11  | 27/10/2026 | FoodStorage        | FIFO circular queue: enqueue, dequeue, peek, isEmpty, isFull, getAll, search                                      |
| 12  | 27/10/2026 | FoodStorageApp     | Console menu with input validation and error handling                                                             |
| 13 | 27/10/2026 | JUnit 5 tests       | 21 unit tests for FoodStorage, FoodItem and FoodType                                                              |
| 14 | 27/10/2026 | Complexity analysis | Time complexity of each operation added to this README                                                            |



### 🔜 Next steps
- [x] `FoodType` – display names and parsing user input
- [x] `FoodItem` – constructor, validation (weight, best-before ≤ 14 days), getters, `toString`
- [x] `FoodStorage` – `addFront`, `isEmpty`, `isFull`
- [x] `FoodStorage` – `peek`, display, `search`
- [x] `FoodStorageApp` – menu with input validation and error handling
- [x] JUnit 5 tests
- [x] Time-complexity analysis
- [ ] Poster (PPT) and video

---

## ⏱️ Time-complexity analysis

We used a **queue built on a circular array** with 8 positions (one for each tray).
The variable `head` shows which tray leaves next (opposite side) and `tail` shows where the next tray goes in (front side).
When one of them gets to the end of the array, it goes back to the position 0 using `% capacity`.
Because of this, we do not need to move the trays inside the array – we only move the two indexes.

| Operation | Method | Time | Why |
|---|---|---|---|
| Add a tray | `enqueue()` | O(1) | puts the tray at `tail` and moves `tail` one step |
| Remove a tray | `dequeue()` | O(1) | takes the tray at `head` and moves `head` one step, nothing is shifted |
| See the top | `peek()` | O(1) | just reads `trays[head]` |
| Show all trays | `getAll()` | O(n) | has to go through every tray once |
| Search by name | `search()` | O(n) | the trays are not sorted, so we check them one by one |
| isEmpty / isFull / size | | O(1) | we keep a `size` counter, so no loop is needed |

In the table, **n** means the number of trays in the storage at that moment (maximum 8).
So O(1) means the time is always the same, and O(n) means the time grows with the number of trays.

### Why we chose this structure
In the scenario the trays go in on one side and come out on the other, so the first tray in is the first one out. This is exactly how a FIFO queue works.
It also makes sense for food: usually, the oldest tray is the one closest to its best-before date, so it should leave first.
According to (Hughes, 2017) by keeping foods that will expire sooner at the front, the FIFO system ensures that this food is used first.
The storage has a fixed size of 8, so a fixed array was enough and we didn't need anything that grows.
We used a circular array instead of a normal one because in a normal array, every time you remove the first item you have to move all the others one position,
which is O(n). With the circular array, adding and removing are always O(1).
According to the research (Woltmann, 2022) enqueue() and dequeue() write or read one array field and advance one index – no matter how many elements the queue contains.

### Limitations
- The size is fixed. If the restaurant wanted more than 8 trays, we would need a bigger array.
- Searching is O(n). With only 8 trays this is not a problem, but with thousands of items a HashMap would be faster.
- You can only take out the oldest tray. It is not possible to remove a tray from the middle.

### Testing
We wrote 21 JUnit 5 tests (`FoodStorageTest` and `FoodItemTest`). They check: what happens when the storage is empty, that the 9th tray is not accepted, that
trays leave in FIFO order, that peek does not remove anything, that the circular array works after going around many times, and that wrong weights, dates and food
names are rejected (including the limit values like 1 g, 5000 g and 14 days).


## 👥 Team 

- Andre Luiz Oliveira da Cunha - 2024214
- Brenda Lima Araujo - 2024264


## 🤖 Use of Generative AI
TODO

## 📚 References

- Hughes, L.B. (2017). Implementing a FIFO food storage system. [online] The Hub | High Speed Training. Available at: https://www.highspeedtraining.co.uk/hub/fifo-food-storage/ [Accessed 27 Oct. 2026].
- Woltmann, S. (2022). Implementing a queue using an array. [online] HappyCoders.eu. Available at: https://www.happycoders.eu/algorithms/implement-queue-using-array/ [Accessed 28 Oct. 2026].