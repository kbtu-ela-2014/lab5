using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Data.Entity;

namespace Lab5.Models
{
    public class FactorizationAnswer
    {
        public int id { get; set; }
        public String typeOfAnswer { get; set; }
        public long task_id { get; set; }
        public List<long> ans { get; set; }
        public String answer { get; set; }
        public long elapsedMS { get; set; }
        public long number { get; set; }
    }

    public class FactorizationAnswerDBContext3 : DbContext
    {
        public DbSet<FactorizationAnswer> Answers { get; set; }
    }
}