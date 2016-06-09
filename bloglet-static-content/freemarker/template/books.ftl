<#macro book name genres authors>
<div class="book-entry">
  <h3>${name}</h3>
  <p>Genres:&nbsp;
    <#list genres as genre>
      <span>${genre}</span>
    </#list>
  </p>
  <p>Authors:&nbsp;
    <#list authors as author>
      <span>${author}</span>
    </#list>
  </p>
  <hr/>
</div>
</#macro>

<#macro books bookList>
<#list bookList as bookModel>
<@book name=bookModel.name genres=bookModel.genres authors=bookModel.authors/>
</#list>
</#macro>