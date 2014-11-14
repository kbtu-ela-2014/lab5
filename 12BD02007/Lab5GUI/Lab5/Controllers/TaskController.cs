using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ServiceLayer;
using TaskLib;

namespace Lab5.Controllers
{
    public class TaskController : Controller
    {
        //
        // GET: /Task/

        public ActionResult Index()
        {
            return View(TaskManager.ReturnAllTasks());
        }

        public ActionResult Create(FormCollection FormColl)
        {
            var ct = FormColl.GetValues("CreateTask");
            /*try
            {
                t.InsertOnSubmit(new Student(cs[0], Convert.ToDecimal(cs[1])));
            }
            catch (Exception e)
            {
                t.InsertOnSubmit(new Student(cs[0], Convert.ToDecimal(cs[1].Replace('.', ','))));
            }*/

            TaskManager.NewTask((Tasks)Enum.Parse(typeof(Tasks), ct[0]), int.Parse(ct[1]), int.Parse(ct[2]));

            //dc.SubmitChanges();
            return Redirect("/");
        }

        public ActionResult TaskResult()
        {
            //var ct = FormColl.GetValues("CreateTask");
            var id = int.Parse(Request["id"]);
            /*try
            {
                t.InsertOnSubmit(new Student(cs[0], Convert.ToDecimal(cs[1])));
            }
            catch (Exception e)
            {
                t.InsertOnSubmit(new Student(cs[0], Convert.ToDecimal(cs[1].Replace('.', ','))));
            }*/


            //dc.SubmitChanges();
            //return Redirect("/TaskResult.aspx");
            return View(TaskManager.ReturnAllTasks().FirstOrDefault<Task>(item => (item.id == id)));
        }
    }
}
