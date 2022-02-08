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
  Footer,
  Text,
} from 'grommet';
import { Notification, FormClose, View, Github } from 'grommet-icons';
import { grommet } from 'grommet/themes';
import React, { useState } from 'react';
import ComboBox from './components/ComboBox';

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
        rows={['xxsmall', 'full', 'xxsmall']}
        columns={['medium', 'full', 'full']}
        gap='xxsmall'
        areas={[
          { name: 'header', start: [0, 0], end: [1, 0] },
          { name: 'nav', start: [0, 1], end: [0, 1] },
          { name: 'main', start: [1, 1], end: [1, 1] },
          { name: 'footer', start: [0, 2], end: [1, 2] },
        ]}
      >
        <Box gridArea='header' background='brand' />
        <Box gridArea='nav' background='light-1'>
          <Box pad='medium' direction='row'>
            <FormField label='Lecturer' htmlFor='lecturer'>
              <ComboBox />
            </FormField>
            <Button icon={<View size='medium' />} margin={{ top: 'medium' }} />
          </Box>
        </Box>
        <Box gridArea='main' background='light-3' />
        <Footer gridArea='footer' background='light-5' pad='small'>
          <Github size='medium' />
        </Footer>
      </Grid>
    </Grommet>
  );
}

export default App;
