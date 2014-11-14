using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TaskLib
{
    public enum Tasks { FACTORIAL, TESTPRIME};

    abstract public class Task
    {
        public List<SubTask> subtasks = new List<SubTask>();
        public object result = null;
        public int id;
        public int number;
        public Tasks type { get; private set; }

        public Task()
        {
            this.id = -1;
            this.number = -1;
        }

        public Task(int number, int id, Tasks t)
        { 
            this.id = id;
            this.number = number;
            this.type = t;
        }

        public static Task CreateTask(Tasks t, int number, int numberOfParall, int id)
        {
            if (numberOfParall < 1)
                numberOfParall = 1;

            Task ans = null;

            switch (t) 
            {
                case Tasks.FACTORIAL:
                    ans = new TaskFactorial(number, id, t);
                    break;
                case Tasks.TESTPRIME:
                    ans = new TaskPrime(number, id, t);
                    break;
            }

            ans.CreateSubTasks(numberOfParall);

            return ans;
        }

        abstract public void group();

        abstract protected void CreateSubTasks(int numberOfParall);

        protected void SplitOnSubTasks(int numberOfParall)
        {
            this.SplitOnSubTasks(numberOfParall, this.number);
        }

        protected void SplitOnSubTasks(int numberOfParall, int numberToSplit)
        {
            int prev = 2;

            int maxNumberOfParall = Math.Max(numberToSplit / 4, 1);

            if (maxNumberOfParall < numberOfParall)
                numberOfParall = maxNumberOfParall;

            for (int i = 0; i < numberOfParall; i++)
            {
                int cur = numberToSplit * (i + 1) / numberOfParall;
                SubTask tp = new SubTask
                {
                    lowerBound = prev,
                    upperBound = cur,
                    number = this.number,
                    id = this.id,
                    type = this.GetType().ToString()
                };
                this.subtasks.Add(tp);
                prev = cur + 1;
            }

        }

        public bool IsAllSubTasksExecuted()
        {
            return (this.subtasks.Where<SubTask>(x => x.answer != null).Count<SubTask>() == this.subtasks.Count);
        }

        public void SetResultOfSubTask(SubTask st)
        {
            this.subtasks.FirstOrDefault<SubTask>(x => (x.upperBound == st.upperBound && x.lowerBound == st.lowerBound)).answer = st.answer;
        }
    }
}
