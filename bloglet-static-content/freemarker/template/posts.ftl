
<#macro blogPost id title shortContents contents tags>
<div class="post-entry">
  <h3><a href="#/post/${id}">${title}</a></h3>
  <p><a href="#">${shortContents}</a></p>
  <ul class="list-inline">
    <li>Tags:</li>
    <#list tags as tag>
      <li><a href="#/tag/${tag.id}">${tag.name}</a></li>
    </#list>
  </ul>
  <hr/>
</div>
</#macro>

<#macro blogPosts postList>
<#list postList as post>
<@blogPost
  id=post.id
  title=post.contents.title
  shortContents=post.contents.shortContents
  contents=post.contents.contents
  tags=post.tagsList
  />
</#list>
</#macro>
