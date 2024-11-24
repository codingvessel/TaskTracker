# Motivation

Just some exercise to stay fresh.

I programmed this TaskTracker-CLI in regard to the https://roadmap.sh/projects/task-tracker challenge

# Requirements & Boundaries

Due to a challenge constrain, this project uses handmade json serializing and is probably prone to error. I also only tested on windows and
this tool has probably some line-ending issues if used on other OS.

# TaskTrackerCLI

### How to run
```bash
git clone https://github.com/codingvessel/TaskTracker.git

start build.bat to compile javacode

cd builtTaskTrackerCLI

start a bash session via cmd or powershell if not already

java TaskTrackerCLI add "Hello World
```

Here is some example usage of the TaskTrackerCLI:

### Adding a new task

```bash
TaskTrackerCLI add "Buy groceries"
```

### Updating and deleting tasks

```bash
TaskTrackerCLI update 1 "Buy groceries and cook dinner"
TaskTrackerCLI delete 1
```

### Marking a task as in progress or done

```bash
TaskTrackerCLI mark-in-progress 1
TaskTrackerCLI mark-done 1
```

### Listing all tasks

```bash
TaskTrackerCLI list
```

### Listing tasks by status

```bash
TaskTrackerCLI list done
TaskTrackerCLI list todo
TaskTrackerCLI list in-progress
```