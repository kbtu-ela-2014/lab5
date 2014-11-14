using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Numerics;

namespace TaskLib
{
    public class TaskFactorial: Task
    {
        public TaskFactorial() : base() {}

        public TaskFactorial(int number, int id, Tasks t) : base(number, id, t) {}

        static public void run(SubTask t)
        {
            t.answer = (object)"";
            BigInteger ans = 1;

            for (int i = t.lowerBound; i < t.upperBound; i++)
                ans = BigInteger.Multiply(ans, new BigInteger(i));

            t.answer = (object)ans.ToString();

            MQueue.SendSubTask(t, MQueue.ConnectionAnswer);
        }

        override protected void CreateSubTasks(int numberOfParall)
        {
            SplitOnSubTasks(numberOfParall);
        }

        override public void group()
        {
            BigInteger ans = 1;

            foreach (SubTask st in this.subtasks)
                ans = BigInteger.Multiply(ans, BigInteger.Parse((string)st.answer));

            this.result = (object)ans.ToString();
        }
    }
}
