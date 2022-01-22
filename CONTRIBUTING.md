# Welcome to Sampoorna contributing guide <!-- omit in toc -->

Thank you for investing your time in contributing to our project! :sparkles:

In this guide you will get an overview of the contribution workflow from opening an issue, creating a PR, reviewing, and merging the PR.

Use the table of contents icon on the top left corner of this document to get to a specific section of this guide quickly 🚀

## Getting started

To get an overview of the project, read the [README](README.md). Here are some resources to help you get started with open source contributions:

- [Finding ways to contribute to open source on GitHub](https://docs.github.com/en/get-started/exploring-projects-on-github/finding-ways-to-contribute-to-open-source-on-github)
- [Set up Git](https://docs.github.com/en/get-started/quickstart/set-up-git)
- [GitHub flow](https://docs.github.com/en/get-started/quickstart/github-flow)
- [Collaborating with pull requests](https://docs.github.com/en/github/collaborating-with-pull-requests)


### Solve an issue

Scan through our [existing issues](https://github.com/OpenLake/Sampoorna/issues) to find one that interests you. You can narrow down the search using `labels` as filters. You can even create one new issue, if you want to start working on some feature.

### Make Changes

Code contributions—bug fixes, new development, test improvement—all follow a GitHub-centered workflow. To participate in OSS development, set up a GitHub account. Then:

1. Fork the repo by going to the project repo page and using the Fork button. This will create a copy of the repo, under your username. (For more details on how to fork a repository see this [guide](https://docs.github.com/en/get-started/quickstart/fork-a-repo).)

2. Clone down the repo to your local system.

`$ git clone git@github.com:your-user-name/Sampoorna.git`

3. Create a new branch to hold your work.

`$ git checkout -b new-branch-name`

4. Work on your new code. Write and run tests.

5. Commit your changes.

`$ git add -A`

`$ git commit -m "commit message here"`

6. Push your changes to your GitHub repo.

`$ git push origin branch-name`

7. Open a Pull Request (PR). Go to the original project repo on GitHub. There will be a message about your recently pushed branch, asking if you would like to open a pull request. Follow the prompts, compare across repositories, and submit the PR. This will send an email to the committers. You may want to consider sending an email to the mailing list for more visibility. (For more details, see the GitHub guide on PRs.

8. Maintainers and other contributors will review your PR. Please participate in the conversation, and try to make any requested changes. Once the PR is approved, the code will be merged.

*Before working on your next contribution, make sure your local repository is up to date.*

1. Set the upstream remote. (You only have to do this once per project, not every time.)

`$ git remote add upstream git@github.com:OpenLake/Sampoorna`

2. Switch to the local master branch.

`$ git checkout master`

3. Pull down the changes from upstream.

`$ git pull upstream master`

4. Push the changes to your GitHub account. (Optional, but a good practice.)

`$ git push origin master`

5. Create a new branch if you are starting new work.

`$ git checkout -b branch-name`

***Additional resources:***
- [Git documentation](https://git-scm.com/documentation)
- [Resolving merge conflicts](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/addressing-merge-conflicts/resolving-a-merge-conflict-using-the-command-line)

### Pull Request

When you're finished with the changes, create a pull request, also known as a PR.
- Don't forget to [link PR to issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue) if you are solving one.
- Enable the checkbox to [allow maintainer edits](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/allowing-changes-to-a-pull-request-branch-created-from-a-fork) so the branch can be updated for a merge.
Once you submit your PR, a team member will review your proposal. We may ask questions or request for additional information.
- We may ask for changes to be made before a PR can be merged, either using [suggested changes](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/incorporating-feedback-in-your-pull-request) or pull request comments. You can apply suggested changes directly through the UI. You can make any other changes in your fork, then commit them to your branch.
- As you update your PR and apply changes, mark each conversation as [resolved](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/commenting-on-a-pull-request#resolving-conversations).
- If you run into any merge issues, checkout this [git tutorial](https://lab.github.com/githubtraining/managing-merge-conflicts) to help you resolve merge conflicts and other issues.

### Your PR is merged!

Congratulations :tada::tada: The Sampoorna team thanks you :sparkles:. 

Once your PR is merged, your contributions will be publicly visible on the [Sampoorna App](https://github.com/OpenLake/Sampoorna)

