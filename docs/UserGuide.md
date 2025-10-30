---
layout: page
title: User Guide
---

Ethical Insurance is a lightweight desktop contact management tool optimized for independent insurance agents managing 0~1000 customers entries.


* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103-F13-2/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your Ethical Insurance.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar Ethical-Insurance.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Keybinds

The following keybinds are available in Ethical Insurance
![Keybinds](images/Keybinds.png)

- Up Arrow (↑): Goes back to previous command in history.
- Down Arrow (↓): Goes forward to next command in history or empties command box when reach the end of history.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (Up to 15 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing Manuals: `man`

Shows manuals for commands.

Format: `man [COMMAND]`
* If `COMMAND` is not specified, shows a list of all available commands.
* If `COMMAND` is specified, shows the manual for the command.

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Filtered list by tag : `list t/<your tag here>`

Sorts and lists only entries with the respective tag(s) entered.

Format: `list t/<your tag here>` and for multiple tags `list t/<your tag here> t/<your tag here> ...`

### Create folder by tag : `list t/<your tag here> s/`

Create and save custom folder. 

Format: `list t/<your tag here> s/` and for multiple tags `list t/<your tag here> t/<your tag here> ... s/`
Output will be a folder with the names of the tags you have selected.
Example:
- Input: list t/friends t/colleagues s/
- Output: Folder friends & colleagues created

### Delete folder by tag : `list t/<your tag here> d/`

Delete selected folder. Order does not matter for the deleting of folder, as long as
the respective tags are that folder will be deleted.

Format: `list t/<your tag here> d/` and for multiple tags `list t/<your tag here> t/<your tag here> ... d/`


### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​`

* Another person entry with the same `NAME` and `PHONE_NUMBER` is treated as a duplicate entry.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal`

**Parameter restrictions:**
***All parameters contains only printable ASCII characters***

- **Name:** Must start with a letter or number
  - Can contain letters, spaces, and the following special characters only:
  (space) , ( ) / . @ - '
  - Names cannot contain numbers or any other special symbols.
  - Must not exceed 100 characters.

  **Example:** 
  - Valid: Jean-Luc, Tan, Mei Ling, O'Connor, Mary (Ann), Alex @ Home
  - Invalid: John123, Strauß, José, John*, John_

- **Phone:** Must be 3 to 15 digits to account for some special numbers and international numbers.
Only digits (0–9) are allowed, no spaces, letters, or symbols.

  **Example:**
  - Valid: 94567802, 82684533, 66265555, 123456789012345
  - Invalid: +65 98765432, phone123, 9011p041, 9312 1534

- **Email:** Must be a valid email address, with an alphanumeric username, optional special characters (+, _, ., -), an '@', and a domain name.
Must not exceed 254 characters.
  - Example: johndoe@example.com, alice.smith-99@mail.co, user+test@abc-def.com_

- **Address:** Must not be blank, must start with a non-space character, and must not end with a space.
Only printable ASCII characters are allowed. Must not exceed 254 characters.

  **Example:**
  - Valid: 123 Main St, Block 5, #01-01, 42 Wallaby Way, 7th Avenue, Apt 3
  - Invalid: " ", " Blk 456, Den Road, #01-355", "Blk 456, Den Road, #01-355 "

- **Tag:** Each Person can only have up to 15 tags. Tags can only contain letters, numbers and dash ("-"). Tags are case insensitive.
Must not exceed 40 characters.
  - Example: friend, VIP, family-member, project2025_

**Criteria for Same Person:**
* Two persons are considered the same if they have the same `NAME` (case-insensitive) and `PHONE_NUMBER`.
* There should not be duplicate persons in the address book.


### Editing a person : `edit`

Edits an existing person in the address book.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [{t/ OR at/ OR dt/}TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When using `t/TAG`, the existing tags of the person will be removed.
* Use `at/TAG` to add on to **existing** tags.
* Use `dt/TAG` to remove from **existing** tags.
* You can only use either of these tag editing commands once per `edit` command.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.
* Each person after editing can only contain a maximum of 15 tags.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.
*  `edit 3 at/InsuranceA` Adds the tag `InsuranceA` on to existing tags of the 3rd person.
*  `edit 3 dt/InsuranceA` Deletes the tag `InsuranceA` from the existing tags of the 3rd person.

Parameter restrictions: Same as `add` command.

### Locating persons by name: `find`

Finds clients whose **name, phone number, or email** contain any of the given keywords (case-insensitive, partial matches allowed).
Format: `find KEYWORD [MORE_KEYWORDS]`

**Details:**
* The search is **case-insensitive**.  
  e.g. `hans` will match `Hans`, `HANS`, or `Johanssen`
* **Partial matches** are supported.  
  e.g. `li` will match `David Li`, `Lina Tan`, and `charlie@gmail.com`
* The search covers **name**, **phone**, and **email** fields.
* The order of the keywords does **not** matter.  
  e.g. `Alex 9123` returns all clients with “Alex” in their name or “9123” in their phone number.
* Matching uses **OR semantics** – a client is listed if **any** keyword matches any searchable field.
* Blank or invalid inputs (e.g. only spaces or non-printable Unicode) are safely ignored.

---

**Examples:**

| Command                    | Description / Result                                                      |
|----------------------------|---------------------------------------------------------------------------|
| `find alex`                | Returns all clients with “alex” in their name, phone, or email.           |
| `find 9123`                | Returns clients whose phone number contains `9123`.                       |
| `find gmail.com`           | Returns clients using Gmail addresses.                                    |
| `find alex 9123 gmail.com` | Returns any client whose name, phone, or email contains any of the terms. |

Examples:
* `find al` returns `Alex Yeow` and `Roy Balakrishnan`
    ![result for 'find al'](images/findAlexResult.png)


* `find alex 9927` returns `Alex Yeoh`, `Bernice Yu`<br>
    ![result for 'find alex 9927'](images/findAlexBerniceResult.png)


### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Declaring Features and Tags for Statistics: `biz`

Declares Features and Tags to group Tags by Features for statistics. Adding those Categories as tags to People will allow them to be considered for statistics.
Applying this command on a Feature with the same name will overwrite the existing Feature-Tags pair. This command is undoable.

Format: `biz f/<your feature here> t/<tag 1> t/<tag 2>`

* Add multiple tags fro aggregation of a Feature.

Examples: [See result in 'Viewing Summary Statistics']
* `biz f/Plan t/A t/B t/C` declares the Feature "Plan" and the Categories "A", "B" and "C" for statistics. 
* `biz f/Gender t/Male t/Female t/Other` declares this Feature and its Categories.

**Parameter restrictions:**
***All parameters contains only printable ASCII characters***
- **Feature and Tag:** A tag name should contain only English letters, digits, or '-' (dash). It must start and end with a letter or digit, and must not exceed 40 characters. Tags are case-insensitive.
  _Example: friend, VIP, family-member, project2025_


### Undeclaring Features and Tags from Statistics: `unbiz`

Undeclares Features and their corresponding Tags. 
This command is undoable.

Format: `unbiz f/<your feature 1 here> f/<your feature 2 here>`

* Undeclare multiple Features by chaining `t\` prefixes together.

Examples: 
* `biz f/Plan` undeclares the Feature "Plan" and its associated tags from statistics.
* `biz f/Gender f/Plan` undeclares these Features: "Plan", "Gender".

**Parameter restrictions:**
***All parameters contains only printable ASCII characters***
- **Feature and Tag:** A tag name should contain only English letters, digits, or '-' (dash). It must start and end with a letter or digit, and must not exceed 40 characters. Tags are case-insensitive.  
  _Example: friend, VIP, family-member, project2025_


### Viewing Summary Statistics: `stats`

Shows Summary Statistics on Customers in the address book.

Statistics will be summarized according to Features and Tags declared by User using the `biz` command.


Format: `stats`

**Example:**
If the following was declared in `biz` command, 

Key - Features: Tags
* Plan: A, B, C
* Gender: Male, Female, Other

Total Number of Customers: 6

Gender  | Number of people
Male   | 0
Female  | 0
Other  | 0

Total for Feature: 0
Average: 0.00
Max Tag: Male & Female & Other (0 people)
Min Tag: Male & Female & Other (0 people)
---------------------------------------------

Plan | Number of people
A   | 1
B   | 0
C   | 0

Total for Feature: 1
Average: 0.33
Max Tag: A (1 person)
Min Tag: B & C (0 people)
---------------------------------------------

### Undoing the last action : `undo`

Reverts the most recent mutable action (add, delete, clear, or edit) performed during the current runtime.

Format: `undo`

* Only actions that change the address book (add, delete, clear, edit, biz, unbiz) can be undone.
* Multiple undo operations can be performed in sequence to revert several actions, as long as they are all mutable actions from the current session.
* Undo is only available for actions performed since the application was started (current runtime).

**Examples:**
* After adding a person, running `undo` will remove the newly added person.
* After deleting a person, running `undo` will restore the deleted person.
* After editing a person, running `undo` will revert the changes made.
* After clearing the address book, running `undo` will restore all previously deleted entries.

---

### Redoing the last undone action : `redo`

Reapplies the most recent sequence of undone mutable actions, as long as no new action has been performed since the last undo.

Format: `redo`

* Only actions that were previously undone using `undo` can be redone.
* If you perform a new action (add, delete, clear, edit) after undoing, the redo history is cleared and you cannot redo the previous actions.
* Multiple redo operations can be performed in sequence to reapply several undone actions, as long as no new action has interrupted the sequence.

**Examples:**
* After undoing an add, running `redo` will add the person back again.
* After undoing a delete, running `redo` will delete the person again.
* If you undo an edit and then perform a new add, you cannot redo the undone edit.


### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

Ethical Insurance address book data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

Ethical Insurance address book data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, Ethical Insurance will discard all data and start with an empty data file at the next run, *without any warning*. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause Ethical Insurance to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Ethical Insurance home folder.

**Q**: Why when I issue add/edit command, something appears on the right side of the application?<br>
**A**: When issuing an add or edit command, a live preview of what you are adding or editing will appear on the right side of your screen. This allows you to quickly verify that the information you entered is correct. When an invalid field is detected (e.g., `n/X Æ A-12`), that field will be highlighted in red to indicate an error. Currently, duplicate users are not flagged (coming soon!).

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Manual** | `man [COMMAND]` <br> e.g., `man add`
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**List by tag** | `list t/<your tag here>` <br> e.g., `list t/friends`
**Create and save folder** | `list t/<your tag here> s/` <br> e.g., `list t/friends s/`
**Delete folder** | `list t/<your tag here> d/` <br> e.g., `list t/friends d/`
**Help** | `help`
**Declare Features and Tags** | `biz f/[FEATURE] t/[TAG]...` <br> e.g. `biz f/Plan t/A t/B`
**Undeclare Features and Tags** | `unbiz f/[FEATURE]...` <br> e.g. `unbiz f/Plan f/Gender`
**Stats**| `stats`
**Undo** | `undo`
**Redo** | `redo`
**Exit** | `exit`
