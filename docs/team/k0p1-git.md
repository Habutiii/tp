---
layout: page
title: K0P1-Git's Project Portfolio Page
---

### Project: Ethical Insurance

Ethical Insurance is a modernized version of AddressBook-Level 3, re-imagined as a lightweight desktop contact management tool tailored for independent insurance agents managing 0 – 1000 customer entries.
Given below are my contributions to the project.

* **Code Contributions:**
  * Major file changes:
    * `FindCommand.java`
    * `ManCommandParser.java`
    * `ManCommand.java`
    * `ClientMatchesPredicate.java`
    * Amongst the abovementioned files, I have also made changes to various other files to ensure seamless integration of new features.
  
  * **New Feature Implemented**: Manual(Man) Command
    * What it does: Provides users with in-app documentation for all available commands, enhancing user experience by allowing quick access to command usage without leaving the application.
    * Justification: This feature improves usability by providing immediate assistance to users, reducing the need to refer to external documentation.
    * Highlights:
      * Implemented `ManCommand` to display command manuals.
      * Created `ManCommandParser` to handle user input for the Man command.
        * Updated the User Guide and Developer Guide to include information about the new Man command.
        * Added JUnit test cases to ensure the functionality of the Man command.
    
  * **Enhancement to Existing Feature**: Find Command 
    * What it does: Enhanced the existing Find command to support searching across multiple fields (name, phone number, email) with partial and case-insensitive matches.
    * Justification: This enhancement makes the Find command more versatile and user-friendly, allowing users to locate contacts more efficiently.
    * Highlights:
      * Modified `FindCommand` to accept multiple search criteria.
      * Created `ClientMatchesPredicate` to encapsulate the logic for matching clients based on various fields.
      * Updated JUnit test cases to cover the new functionality of the Find command.

   * **Additional Improvements**:
     * Added tag limit validation (max 15 tags per person) to improve UI/UX performance and prevent excessive tagging. 
     * Added associated tests to ensure validation works as intended.


* **Project Management:**
  * Assisted in sprint planning and retrospective sessions to improve team workflow.
  * Helped coordinate tasks among team members to ensure timely completion of features.
  * Assisted in managing the project repository, including branching strategies and pull request reviews.
    * Code reviews for peers to ensure code quality and adherence to project standards.
  * Contributed to the creation and maintenance of the project repository on GitHub and the setup of CI/CD pipelines.


* **Documentation**:
  * User Guide:
    * Updated sections related to the Find command to reflect new search capabilities.
    * Added a new section on the ManCommand, detailing its usage and benefits.
    * Refactored the majority of the existing documentation to improve clarity and coherence.
  * Developer Guide:
    * Added PUML sequence diagram to illustrate the find command logic.
    * Created detailed explanations of the find command's implementation.
