/**
 * 
 */

//document.getElementById("edit").innerHTML = "test"; 

//$(function() {
//
//	$('.js-articlesPage').click(function(event) {
//		var parents = $(event.target)
//			.parents('.detailsRow')
//			.toggleClass('open');
//
//	});
	
//	
//$(document).ready(function() {
//	$(.'js-articlesPage').click(function(event) {
//		var parents = $(event.target)
//			.parents('.detailsRow')
//			.toggleClass('open'); 
//	});	
//}); 


//for (i = 0; i < rows.length; i++) {
//	var currentRow = table.rows[i];
//	var createClickHandler = function(row) {
//		return function() {
//			var previewId = 'articlePreview' + row.dataset.articleId;
//			var preview = document.getElementById(previewId); 
//
//			if (preview.classList.contains('open')) {
//				preview.classList.remove('open');
//			} else {
//				preview.classList.add('open');
//			}
//		};
//	};
//
//	currentRow.onclick = createClickHandler(currentRow);
//}
	
//	var buttons = document.querySelectorAll('#articlesContainer button');
//	
//	[].forEach.call(buttons, function(b) {
//		b.addEventListener('click', function(e) {
//			var previewId = 'articlePreview' + b.dataset.articleId; 
//			var preview = document.getElementById(previewId); 
//	//		var icon = b.querySelector('i'); 
//	//		
//			if (preview.classList.contains('open')) {
//				preview.classList.remove('open'); 
//			} else {
//				preview.classList.add('open'); 
//			}
//		}); 
//	}); 
//}); 




// works
//var rows = document.getElementById("overviewTable").getElementsByTagName("tr"); 
var rows = document.querySelectorAll(".clickable"); 

[].forEach.call(rows, function(row) {
	row.addEventListener('click', function(e) {
		var previewId = 'articlePreview' + row.dataset.articleId; 
		var preview = document.getElementById(previewId); 

		if (preview.classList.contains('open')) {
			preview.classList.remove('open'); 
		} else {
			preview.classList.add('open'); 
		}
	}); 
}); 
