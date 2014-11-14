using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Messaging;
using System.Threading;
using TaskLib;

namespace ServiceLayer
{
    public class TaskManager
    {
        static List<Task> tasksInProcess = new List<Task>();
        static List<Task> tasksFinished = new List<Task>();
        static int counter = 0;
        static Thread trd;

        static TaskManager()
        {
            if (!MessageQueue.Exists(MQueue.ConnectionTask))
                MessageQueue.Create(MQueue.ConnectionTask);
            if (!MessageQueue.Exists(MQueue.ConnectionAnswer))
                MessageQueue.Create(MQueue.ConnectionAnswer);

            AddChecking(TaskManager.CheckAnswer);
        }

        public static List<Task> ReturnAllTasks()
        {
            return tasksFinished.Concat<Task>(tasksInProcess).OrderBy(task => task.id).ToList<Task>();
        }

        static void AddChecking(Action a){
            trd = new Thread(new ThreadStart(a));
            trd.Start();
        }

        static public void Dispose()
        {
            trd.Abort();
        }

        static void Add(Task t) 
        {
            tasksInProcess.Add(t);
            foreach (SubTask st in t.subtasks)
                MQueue.SendSubTask(st, MQueue.ConnectionTask);
            
            if (trd.ThreadState == ThreadState.Suspended)
                trd.Resume();
        }

        public static int NewTask(Tasks t, int number, int numberOfParall) 
        {
            TaskManager.counter++;
            Task tsk = Task.CreateTask(t, number, numberOfParall, TaskManager.counter);
            TaskManager.Add(tsk);

            return TaskManager.counter;
        }

        public static void makeFinished(Task t)
        {
            TaskManager.tasksFinished.Add(t);
            TaskManager.tasksInProcess.Remove(t);
        }

        static void CheckAnswer() 
        {
            while (true)
            {
                if (tasksInProcess.Count == 0)
                    trd.Suspend();

                 SubTask st = MQueue.ReceiveSubTask(MQueue.ConnectionAnswer);
                 Task hk = (Task)tasksInProcess.FirstOrDefault<Task>(task => (task.id == st.id));
                 hk.SetResultOfSubTask(st);

                 if (hk.IsAllSubTasksExecuted())
                 {
                     hk.group();
                     TaskManager.makeFinished(hk);
                 }
            }
        }

        public static object GetAnswerById(int id) 
        {
            Task t = tasksFinished.FirstOrDefault<Task>(x => x.id == id);
            return (t == null) ? null : t.result;
        }
    }
}
