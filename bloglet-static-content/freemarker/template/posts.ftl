
<#macro blogPost id title dateCreated shortContents contents tags>
<div class="post-entry">
  <h3><a href="/g/post/${id}">${title?html}</a></h3>
  ${shortContents}
  <p><a href="/g/post/${id}">Read More...</a></p>
  <div class="entry-footer">
    <ul class="list-inline pull-left">
      <li>Tags:</li>
      <#list tags as tag>
        <li><a href="/g/tag/${tag.id}">${tag.name}</a></li>
      </#list>
    </ul>
    <span class="pull-right text-muted">${dateCreated?number_to_datetime}</span>
  </div>
  <hr/>
</div>
</#macro>

<#macro blogPosts postList>
<#list postList as post>
<@blogPost
  id=post.id
  title=post.contents.title
  dateCreated=post.contents.dateCreated
  shortContents=post.contents.shortContents
  contents=post.contents.contents
  tags=post.tagsList
  />
</#list>
</#macro>
