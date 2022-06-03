# Contributing Guide

This project adheres to
[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/).

In case you need more information about it, see [Angular Commit Message Format](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit) and [a simple tutorial](https://medium.com/swlh/git-write-quality-commits-be0f6958114).

## Commit Message Specifications

```git
<type>([optional scope]): <description>

[optional body]

[optional footer(s)]
```

- `type`
  - fix:  code changes that fix a bug. PATCH in semantic versioning.
  - feat:  code changes that add a new feature. MINOR in semantic versioning.
  - refactor: code changes that neither fix a bug nor add a feature.
  - build: changes in the build system or external dependencies.
  - chore: changes in auxiliary tools.
  - docs: changes in documentation files.
  - revert: a commit that reverts another commit.

    ```git
    revert: <header of the reverted commit>

    This reverts the commit <commit-SHA>

    [optional reason for reverting]
    ```

- `scope`: a noun describing a section of the code base surrounded by parenthesis (usually the directory of the activity one is modifying).
