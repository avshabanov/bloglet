<#import "../template/page-wrapper.ftl" as pw/>
<#import "../template/books.ftl" as b/>

<@pw.page title="Main">

<h2>Books</h2>

<div id="book-container" class="container">
  <@b.books bookList=books />
</div>

<div class="container load-more-container">
  <p>Click 'Load More' to fetch more books</p>
  <div>
    <button id="btn-load-more" type="button" class="btn btn-primary">Load More</button>
  </div>
</div>

</@pw.page>