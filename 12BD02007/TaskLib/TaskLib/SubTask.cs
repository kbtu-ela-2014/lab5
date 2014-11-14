using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TaskLib
{
    [Serializable]
    public class SubTask
    {
        public int number { get; set; }
        public int lowerBound { get; set; }
        public int upperBound { get; set; }
        public int id { get; set; }
        public object answer { get; set; }
        public string type {get; set; }

        public SubTask(int lowerBound, int upperBound, int number)
        {
            this.number = number;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.answer = null;
            this.type = "";
        }

        public SubTask() {
            number = lowerBound = upperBound = 0;
            this.answer = null;
            this.type = "";
        }

        public void Run()
        {
            Type t = Type.GetType(this.type);
            t.GetMethod("run").Invoke(null, new object[] { this });
        }
    }
}
