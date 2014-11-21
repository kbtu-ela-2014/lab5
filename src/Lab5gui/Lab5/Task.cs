using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Lab5
{
    [Serializable]
    public class Task
    {
        public long number { get; set; }
        public string todo{get;set;}
        public long id{get;set;}
    }
}