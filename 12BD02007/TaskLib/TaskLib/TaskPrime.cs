using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace TaskLib
{
    public class TaskPrime:Task
    {
        public TaskPrime() : base() {}

        public TaskPrime(int number, int id, Tasks t) : base(number, id, t) {}

        public static void run(SubTask t)
        {
            t.answer = (object)1;

            for (int i = t.lowerBound; i < t.upperBound; i++)
            {
                if (t.number == i)
                    break;

                if (t.number % i == 0)
                {
                    t.answer = (object)0;
                    break;
                }
            }

            MQueue.SendSubTask(t, MQueue.ConnectionAnswer);
        }

        override protected void CreateSubTasks(int numberOfParall)
        {
            int nsq = (int)Math.Sqrt(number) + 1;
            this.SplitOnSubTasks(numberOfParall, nsq);
        }

        override public void group()
        {
            this.result = (object)((short)((this.subtasks.Aggregate(0, (total, x) => total + (int)x.answer) == this.subtasks.Count) ? 1 : 0));
        }

    }
}
