# File Rename Helper

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](/docs/LICENSE.md)

GUI application for renaming and moving (primarily PDF) files, leveraging AI to provide intelligent renaming suggestions based on file content.

## Table of Contents

*   [Overview](#overview)
*   [Features](#features)
*   [Getting Started](#getting-started)
*   [Project Status & Roadmap](#project-status-roadmap)
*   [Contributing](#contributing)
*   [License](#license)
*   [Project Decisions](#project-decisions)
*   [Tasks and Known Issues](#tasks-and-known-issues)

---

## Overview

This project provides a user-friendly interface to simplify the often tedious process of renaming and organizing files.  By analyzing the content of files (especially PDFs), the application offers AI-powered recommendations for new filenames, making it easier to manage large collections of documents.

## Features

*   **AI-Powered Suggestions:**
    * Get filename recommendations based on the content of your files.
    * Get file path recommendations based on file content.
*   **Background suggestion generation:**  Generate suggestions in the background so you will not have to wait for suggestions between each file.

## Getting Started
You can download the latest Proof Of Concept release here: [v0.0.1-POC.1](https://github.com/koen-mulder/file-rename-helper/releases/tag/v0.0.1-POC.1). That page also contains instructions on how to run the application.

*(Currently, the project is under active development. Instructions on compiling and running the application will follow later):*

## Project Status & Roadmap

**Recently Added to main but not released yet:**

- [x]  **Suggestion of relevant words and dates:** Using the chosen LLM to suggest relevant dates from the document. This is useful if you would like to add the send date of a letter to the filename.
- [x]  **Background suggestion generation:** Added functionality for selecting multiple files and passing them to the LLM for suggestions in the background.

**Next Up:**

- [ ] **Saving renamed files:** Rather crucial for a file rename helper. This will be in the next release.
- [ ] **Suggesting file paths:** Suggest file locations using a list of preferred categories, a base destination path and a path format.
- [ ] **Keep chat message log for a file:** Because of the background processing, there is a separation between initial processing of a file and requesting more suggestions. Because of this the chatlog with the LLM is not kept. This should be changed.
- [ ] **Use Retrieval-Augmented Generation (RAG) for suggestions:** Use existing files and recent processed files to help suggesting file names and paths.
- [ ] **Test other LLMs:** Make a list of OpenSource LLM's that work with this application.


## Contributing

Contributions are welcome!  We encourage you to help improve this project.  Here's how you can contribute:

*   **Submit Feature Requests:**  Have an idea for a new feature?  Open an issue and describe your suggestion.
*   **Report Bugs:**  Found a bug?  Create an issue with a clear description of the problem and steps to reproduce it.
*   **Contribute Code:**  Want to help with development?  Fork the repository, make your changes, and submit a pull request.

Before contributing, please read our:

*   [Contributing Guidelines](/docs/CONTRIBUTING.md)
*   [Code of Conduct](/docs/CODE_OF_CONDUCT.md)

## License

This project is licensed under the Apache License Version 2.0.  See the [LICENSE](/docs/LICENSE.md) file for details.

## Project Decisions

For a detailed explanation of the design choices and reasoning behind them, please see the [Choices document](/docs/choices.md).

## Tasks and Known Issues

A comprehensive list of current tasks, planned features, and known bugs can be found in the [Tasks document](/docs/tasks.md).