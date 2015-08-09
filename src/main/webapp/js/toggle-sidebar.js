'use strict';

// Small script for toggling the sidebar with the results, according to input.    
        var wrapper = document.getElementById( 'wrapper' ),
        topbar = document.getElementById( 'topbar' ),
        showSidebar = document.getElementById( 'show-sidebar' ),
        body = document.body;

        showSidebar.oninput = function() {
  
             if(wrapper.className.indexOf("toggled") != -1) {
                wrapper.className = wrapper.className.replace( "toggled", ' ' );
                topbar.className = topbar.className + " showTopbar";  
            }
          
            if(showSidebar.value.length == 0) {
                wrapper.className = wrapper.className + " toggled";
                topbar.className = topbar.className.replace( "showTopbar", ' ' );
            }

        };


