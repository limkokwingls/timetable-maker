import {
  Box,
  Button,
  Layer,
  Collapsible,
  Heading,
  Grommet,
  Main,
  Paragraph,
  Grid,
  FormField,
  Select,
} from 'grommet';
import { Notification, FormClose, View } from 'grommet-icons';
import { grommet } from 'grommet/themes';
import React, { useState } from 'react';

const theme = {
  global: {
    colors: {
      brand: '#228BE6',
    },
    font: {
      family: 'Roboto',
      size: '18px',
      height: '20px',
    },
  },
};

function App() {
  return (
    <Grommet full theme={grommet}>
      <Grid
        fill
        rows={['xxsmall', 'full']}
        columns={['medium', 'full']}
        gap='small'
        areas={[
          { name: 'header', start: [0, 0], end: [1, 0] },
          { name: 'nav', start: [0, 1], end: [0, 1] },
          { name: 'main', start: [1, 1], end: [1, 1] },
        ]}
      >
        <Box gridArea='header' background='brand' />
        <Box gridArea='nav' background='light-1'>
          <Box pad='medium' direction='row'>
            <FormField label='Lecturer' htmlFor='lecturer'>
              <Select
                size='small'
                id='lecturer'
                placeholder='Lecturer'
                options={['One', 'Two']}
              />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
        </Box>
        <Box gridArea='main' background='light-3' />
      </Grid>
    </Grommet>
  );
}

export default App;
