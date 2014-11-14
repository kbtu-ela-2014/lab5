<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<Task>" %>
<%@ Import namespace="TaskLib" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
	TaskResult
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <h2>TaskResult</h2>
    <div style="word-break: break-all"><%: Model.result.ToString() %></div>

</asp:Content>
