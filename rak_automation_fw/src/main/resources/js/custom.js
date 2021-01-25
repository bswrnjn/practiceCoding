$( function()
{
    if( document.location.pathname.endsWith( 'console.html' ) )
    {
        // avoid duplicate
        $( "#logs" ).html( '' );

        $.get( "console.log", function( data )
        {
            $( data.split( '\n' ) ).each( function( index, log )
            {
                var logComponent = log.split( " " );
                if( logComponent.length > 2 && logComponent[ 2 ] == 'ERROR' )
                {
                    $( "#logs" ).append( '<p><font color="red">' + log + '</font></p>' );
                }
                else
                {
                    $( "#logs" ).append( '<p>' + log + '</p>' );
                }
            } );
        } );
    }
    else
    {
        $( "#includedContent" ).load( "table.html", null, applySorter );
    }
} );

function applySorter()
{
    $( "table" ).tablesorter(
    {
        widgets :
        [ "zebra", "resizable" ]
    } );

    $( "table" ).colResizable();
}