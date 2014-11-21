using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Worker
{
    public class FactorizationAnswerRAW
    {
        public String typeOfAnswer{get;set;}
        public long id { get; set; }
        public List<long> ans { get; set; }
        public String answer { get; set; }
        public long elapsedMS { get; set; }
        public long number { get;set; }
    }
}
