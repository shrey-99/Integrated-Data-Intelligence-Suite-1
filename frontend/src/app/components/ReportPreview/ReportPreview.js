import React from 'react';

const ReportPreview = () => {
    return (
        <div className="main-preview-container">
            <iframe
              title="pdf=preview"
              src="http://mozilla.github.io/pdf.js/web/compressed.tracemonkey-pldi-09.pdf"
              frameBorder="10px"
              scrolling="auto"
              height="100%"
              width="800px"
            />
        </div>
    );
};

export default ReportPreview;
