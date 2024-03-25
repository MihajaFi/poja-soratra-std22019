CREATE TABLE text (
                         id VARCHAR(255) PRIMARY KEY,
                         original_bucket_key VARCHAR(255),
                         upper_bucket_key VARCHAR(255),
                         creation_datetime TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         update_datetime TIMESTAMP WITH TIME ZONE  DEFAULT CURRENT_TIMESTAMP NOT NULL
);
