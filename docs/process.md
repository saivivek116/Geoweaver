
# Processes in Geoweaver

## What is Process?

`Process` means code scripts to process data. 

You can understand it as source code like Python. However, `Process` in Geoweaver is more dedicated to the **source code for data processing**. 

Note: It is not designed for **everything**. You might be able to run any code like starting a HTTP server, or starting a GUI software, but it would be inappropriate and might cause issues. **We recommend users to restrict the Process code to be something that is finishable (no hanging), algorithmic, and data oriented**. 

## Create a new Python process

1. Click the plus icon button after `Process` on the right panel.

2. Select `Python` from the `Language` dropdown select.

3. Input `helloworld` for the name.

4. Add code to the code area. For example:

```Python
print("hello world")
```

5. Click `Add` on the bottom. A new process node `helloworld` will be added to the `Process`>`Python` tree.


## Run Python Process

1. Click on the newly added `helloworld` process. An information panel will show in the main area.

2. Click the play button to run the process. In the popup window, select `Localhost` and click `Execute`. 

3. A dialog will show to specify Python environment, click `Confirm` to the `default`. 

4. In the password dialog, input your password for `Localhost`. Click `confirm`.

If you see hello world printed in the logging window, it means you have successfully created and run your first process in Geoweaver. 

Congratulations! You did it!

## Supported Code Scripts

Geoweaver supports four types of processes to be executed on the SSH hosts enlisted in the Host section: Shell script, Notebooks, Python code, and Builtin processes.

### Shell

Shell scripts can be directly created, saved, executed, and monitored in Geoweaver. Users can execute the shell scripts on remote servers or the localhost server which Geoweaver is hosted.

### Python

Python is one of the most popular AI programming langauge and most AI-related packages reside in Python. Geoweaver supports Python coding and scripting on top of multiple servers while reserving and maintaining the code in one database. All the historical runs are recorded and served in Geoweaver to prevent future duplicated attempts and significantly improve the reproducibility and reusability of AI programs.

### NoteBook

Geoweaver supports to run Jupyter notebooks using the nbconvert command. The notebook and its logout are recorded in the database.

Running notebook in Geoweaver will be equivalent to running `jupyter nbconvert --inplace --allow-errors --to notebook --execute <notebook_file_path>`.

**New notebook snapshots generated by every run will be all saved in the Geoweaver database**. No more worries on forgetting the results of your previous experiment configuration! Everything is covered.

### Build In Process

To help people with limited programming skills, we are developing a set of built-in processes which have fixed program code and expose only input parameters to users. These processes make Geoweaver a powerful AI graphical user interface for the diverse user groups to learn and experiment their AI workflows without coding. Most buil-in processes in Geoweaver are developed based on the existing AI python ecosystem like Keras and Scikit-learn. This section is under intensive development right and the first stable version expects users to create a full-stack AI workflow without writing a single line of code.

