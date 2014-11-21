using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;


using System.Data.Entity;

namespace Lab5.Models
{
    public class EulerAnswer
    {
        public long number { get; set; }
        public long task_id { get; set; }
        public long result { get; set; }
        public long elapsedMS { get; set; }
        public int id { get; set; }
    }
    

    public class EulerAnswerDBContext3 : DbContext
    {
        public DbSet<EulerAnswer> Answers { get; set; }
    }
}