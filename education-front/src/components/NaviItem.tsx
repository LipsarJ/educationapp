import React from 'react'
import {Flex, Icon, Link, Menu, MenuButton, Text} from '@chakra-ui/react'
import {NavLink, useNavigate} from 'react-router-dom';

export default function NavItem({
                                    icon,
                                    title,
                                    description,
                                    url
                                }: { icon: any, title: string, description: string, url: string }) {
    const navigate = useNavigate();
    return (
        <Flex
            mt={30}
            flexDir="column"
            w="100%"
            alignItems="center"
        >
            <Menu placement="right">
                <Link
                    backgroundColor="white"
                    p={3}
                    borderRadius={8}
                    _hover={{textDecor: 'none', backgroundColor: "#AEC8CA"}}
                    w="100%"
                    as={NavLink} to={url}
                >
                    <MenuButton w="100%">
                        <Flex>
                            <Icon as={icon} fontSize="xl" color="#82AAAD"/>
                            <Text ml={5} display="flex">{title}</Text>
                        </Flex>
                    </MenuButton>
                </Link>
            </Menu>
        </Flex>
    )
}